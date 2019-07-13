package org.lynxz.locationdemo.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.support.annotation.StringRes
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.util.SparseArray
import kotlin.random.Random


/**
 * Created by lynxz on 2019/1/29
 * E-mail: lynxz8866@gmail.com
 *
 * Description: 权限申请结果信息
 */
data class PermissionResultInfo(
    var name: String, // 权限名称
    var granted: Boolean = false, // 是否已被授权
    var shouldShowRequestPermissionRationale: Boolean = false // 用户拒绝授权时,是否同时选中了 "Don’t ask again"
)

/**
 * 权限申请回调
 * */
interface IPermissionCallback {
    /**
     * 某权限的授权申请结果
     * */
    fun onRequestResult(permission: PermissionResultInfo)

    /**
     * 所有权限是否都已被授权
     * @param allGranted false-至少有一个权限被拒绝
     * */
    fun onAllRequestResult(allGranted: Boolean) {}
}

/**
 * v1.0
 * 封装权限申请长流程 Fragment
 * 1. 通过 [isPermissionGranted] 来判断权限是否已被授权
 * 2. 通过 [requestPermissions] 来批量申请权限
 * 3. 通过 [requestPermission] 来申请某个权限的授权
 * 4. 通过 [requestPermissionWithDialogIfNeeded] 来申请某个权限的授权,并按需弹出申请理由dialog
 * 5. 通过 [startSettingActivity] 来跳转到设置页面
 * 使用方法:
 *  <pre>
 *      // 1. 注入权限申请fragment到指定的 activity 中
 *      val permissionFrag = BaseTransFragment.getTransFragment(hostActivity, "permission_tag", PermissionFragment())
 *
 *      // 2. 设置回调接口
 *      val permissionCallback = object : IPermissionCallback {
 *          override fun onRequestResult(permission: PermissionResultInfo) {
 *              // 具体某个权限的授权结果
 *              Logger.d("授权结果\n权限名=${permission.name},是否授权=${permission.granted},是否可再弹出系统权限框=${permission.shouldShowRequestPermissionRationale}")
 *          }
 *
 *          override fun onAllRequestResult(allGranted: Boolean) {
 *              // 所申请的权限是否全部都通过了
 *          }
 *      }
 *
 *      // 3. 申请单个权限
 *      permissionFrag?.requestPermission(Manifest.permission.RECORD_AUDIO, permissionCallback)
 *
 *      // 4. 申请单个权限,并按需弹出dialog跳转到设置页面
 *      permissionFrag?.requestPermissionWithDialogIfNeeded(Manifest.permission.RECORD_AUDIO, "缺少录音权限", "请点击确定按钮到设置页面开启权限", permissionCallback)
 *
 *      // 5. 批量申请权限
 *      permissionFrag?.requestPermissions(arrayOf(Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA), permissionCallback)
 *  </pre>
 */
class PermissionFragment : BaseTransFragment() {
    private val mCallbacks = SparseArray<IPermissionCallback?>()
    private val mSettingPermissionRequest = SparseArray<String>() // 跳转到设置页面进行权限申请的权限名信息
    private val mRandom = Random(System.currentTimeMillis())

    companion object {
        const val CODE_SETTING = 100

        /**
         * 跳转到权限设置页面
         * */
        fun startSettingActivity(from: Activity, requestCode: Int = CODE_SETTING) {
            try {
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                intent.data = Uri.parse("package:${from.packageName}")
                from.startActivityForResult(intent, requestCode)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        /**
         * 判断指定权限是否已被授权
         * */
        fun isPermissionGranted(context: Context, permission: String): Boolean {
            return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                true
            } else {
                ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
            }
        }
    }

    /**
     * 申请指定权限
     * */
    fun requestPermission(permission: String, callback: IPermissionCallback?) {
        if (isPermissionGranted(hostActivity, permission)) {
            callback?.onRequestResult(PermissionResultInfo(permission, true, false))
            callback?.onAllRequestResult(true)
        } else {
            requestPermissions(arrayOf(permission), callback)
        }
    }

    /**
     * 申请权限, 若权限已被拒绝并 "Don’t ask again",则弹出提示框,点击确定按钮则跳转到设置页面
     * */
    fun requestPermissionWithDialogIfNeeded(
        permission: String,
        title: CharSequence? = "",
        msg: CharSequence? = "",
        @StringRes titleResId: Int = 0,
        @StringRes msgResId: Int = 0,
        callback: IPermissionCallback?
    ) {
        val granted = isPermissionGranted(hostActivity, permission)
        val canRequestAgain = !ActivityCompat.shouldShowRequestPermissionRationale(hostActivity, permission)
        if (granted) {
            callback?.onRequestResult(PermissionResultInfo(permission, granted, canRequestAgain))
            callback?.onAllRequestResult(true)
        } else {
            if (canRequestAgain) {
                requestPermissions(arrayOf(permission), callback)
            } else {// 显示提示dialog并跳转设置页面
                val requestCode = generateRequestCode()
                mCallbacks.put(requestCode, callback)
                mSettingPermissionRequest.put(requestCode, permission)
                showRequestDialog(title, msg, titleResId, msgResId, requestCode)
            }
        }
    }

    /**
     * 批量申请权限
     * */
    fun requestPermissions(permissions: Array<String>, callback: IPermissionCallback?) {
        val requestCode = generateRequestCode()
        mCallbacks.put(requestCode, callback)
        requestPermissions(permissions, requestCode)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        val callback = mCallbacks.get(requestCode) ?: return

        var isAllGranted = true // 所申请的权限是否全部通过授权
        for (index in 0 until grantResults.size) {
            val isGranted = grantResults[index] == PackageManager.PERMISSION_GRANTED
            val name = permissions[index]
            val canRequestAgain = ActivityCompat.shouldShowRequestPermissionRationale(hostActivity, name)
            callback.onRequestResult(PermissionResultInfo(name, isGranted, canRequestAgain))
            if (!isGranted) {
                isAllGranted = false
            }
        }
        callback.onAllRequestResult(isAllGranted)
        mCallbacks.remove(requestCode)
    }

    /**
     * 从设置页面返回到本app后,检查权限是否已被授权,并回调通知用户
     * */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val callback = mCallbacks.get(requestCode) ?: return
        val permissionName = mSettingPermissionRequest.get(requestCode) ?: return
        val granted = isPermissionGranted(hostActivity, permissionName)
        val canRequestAgain = ActivityCompat.shouldShowRequestPermissionRationale(hostActivity, permissionName)
        callback.onRequestResult(PermissionResultInfo(permissionName, granted, canRequestAgain))
        callback.onAllRequestResult(granted)
    }

    /**
     * 随机生成权限申请requestCode
     * */
    private fun generateRequestCode(): Int {
        var code = 0
        var tryTimes = 0
        do {
            // 选择 0xFFFF ,可以参考 FragmentActivity 类的 checkForValidRequestCode() 方法
            // 参考: https://blog.csdn.net/barryhappy/article/details/53229238
            code = mRandom.nextInt(0xFFFF)
            tryTimes++
        } while (mCallbacks.indexOfKey(code) >= 0 && tryTimes <= 20)
        return code
    }

    /**
     * 用户申请某权限时,若该权限已被拒绝过,并且用户勾选了 "Don’t ask again"
     * 则弹出提示框,并跳转设置页面
     * */
    private fun showRequestDialog(
        title: CharSequence?,
        msg: CharSequence?,
        @StringRes titleResId: Int = 0,
        @StringRes msgResId: Int = 0,
        requestCode: Int
    ) {
        AlertDialog.Builder(hostActivity).apply {
            if (titleResId != 0) {
                setTitle(titleResId)
            } else {
                setTitle(title)
            }

            if (msgResId != 0) {
                setMessage(msgResId)
            } else {
                setMessage(msg)
            }
        }
            .setPositiveButton(android.R.string.yes) { _, which ->
                try {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    intent.data = Uri.parse("package:${hostActivity.packageName}")
                    startActivityForResult(intent, requestCode)
                } catch (e: Exception) {
                    e.printStackTrace()
                    mSettingPermissionRequest.remove(requestCode)
                }
            }
            .setNegativeButton(android.R.string.no, null)
            .show()
    }
}