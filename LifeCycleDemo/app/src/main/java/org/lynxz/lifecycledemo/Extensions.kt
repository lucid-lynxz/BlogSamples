
import android.app.Activity
import android.content.Context
import android.content.pm.PackageInfo
import android.graphics.Outline
import android.support.annotation.LayoutRes
import android.support.annotation.StringRes
import android.support.v4.app.Fragment
import android.support.v4.content.PermissionChecker
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewOutlineProvider
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import org.lynxz.lifecycledemo.BuildConfig
import java.text.DecimalFormat
import java.text.SimpleDateFormat

/**
 * Created by lynxz on 2019/1/24
 * E-mail: lynxz8866@gmail.com
 *
 * Description: 将View外框形状裁切为圆角
 * @param clipCircle true-裁切为圆形图案
 * @param roundRadiusPx 圆角半径值,单位:px, clipCircle=false时有效,裁切为圆角图形
 */
class CommonRoundOutlineProvider(var clipCircle: Boolean = false,
                                 var roundRadiusPx: Float = 10F) : ViewOutlineProvider() {
    override fun getOutline(view: View?, outline: Outline?) {
        if (view == null || outline == null) return

        if (clipCircle) {
            val size = Math.min(view.measuredWidth, view.measuredHeight)
            val radius = size / 2
            val centerX = view.measuredWidth / 2
            val centerY = view.measuredHeight / 2
            outline.setRoundRect(centerX - radius, centerY - radius, centerX + radius, centerY + radius, radius.toFloat())
        } else {
            outline.setRoundRect(0, 0, view.measuredWidth, view.measuredHeight, roundRadiusPx)
        }
    }
}

/**
 * Created by lynxz on 13/01/2017.
 * update 2019.1.24 v1.4
 * 扩展函数
 */
fun CharSequence.isEmpty(): Boolean {
    return TextUtils.isEmpty(this)
}

fun Context.showToast(msg: String?) {
    if (!msg.isNullOrBlank()) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}

fun Context.showToast(msgId: Int) {
    Toast.makeText(this, msgId, Toast.LENGTH_SHORT).show()
}

fun Context.getStringRes(@StringRes strId: Int): String {
    return resources.getString(strId)
}

fun Fragment.showToast(msg: String?) {
    if (!msg.isNullOrBlank()) {
        Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show()
    }
}

fun Fragment.showToast(msgId: Int) {
    Toast.makeText(activity, msgId, Toast.LENGTH_SHORT).show()
}

fun Fragment.getStringRes(@StringRes strResId: Int): String? {
    return activity?.resources?.getString(strResId)
}

fun Context.isPermissionGranted(permission: String): Boolean {
    return PermissionChecker.checkSelfPermission(this, permission) == PermissionChecker.PERMISSION_GRANTED
}

fun Activity.hideKeyboard() {
    hideKeyBoard(currentFocus)
}

/**
 * 强制隐藏输入法键盘
 */
fun Context.hideKeyBoard(currentFocusView: View?) {
    currentFocusView?.let {
        val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            ?: return
        inputMethodManager.hideSoftInputFromWindow(it.windowToken, 0)
    }
}

/**
 * 保留N位小数,并返回字符串
 * */
fun Double.limit(limit: Int = 2): String {
    val pattern = "0.${"#".repeat(limit)}"
    return DecimalFormat(pattern).format(this)
}

/**
 * double类型向上保留转换为整数,如 2.1 -> 3  2.0->2
 * */
fun Double.toIntUp(): Int {
    val remainder = if (this % 1 > 0) 1 else 0
    return this.toInt() + remainder
}

inline fun debugConf(code: () -> Unit) {
    if (BuildConfig.DEBUG) {
        code()
    }
}

fun String?.convertDateFormat(oriFormat: String = "yyyy-MM-dd'T'HH:mm:ss", newformat: String = "yyyy.MM/dd HH:mm"): String {
    return try {
        val oriSdf = SimpleDateFormat(oriFormat)
        val date = oriSdf.parse(this)
        SimpleDateFormat(newformat).format(date)
    } catch (e: Exception) {
        e.printStackTrace()
        ""
    }
}

fun Long?.convertDateFormat(format: String = "yyyy.MM/dd HH:mm"): String {
    if (this == null) return ""
    return try {
        val targetTs = if ("$this".length <= 10) this * 1000 else this
        SimpleDateFormat(format).format(targetTs)
    } catch (e: Exception) {
        e.printStackTrace()
        ""
    }
}

fun ViewGroup.inflate(@LayoutRes layoutRes: Int, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)
}

/**
 * 判断指定的app是否已安装
 * */
fun Context?.isAppInstall(appPackageName: String?): Boolean {
    if (this == null || appPackageName.isNullOrBlank()) return false
    val pInfo: List<PackageInfo>? = packageManager.getInstalledPackages(0) ?: return false
    for (i in pInfo!!.indices) {
        val pn = pInfo[i].packageName
        if (TextUtils.equals(pn, appPackageName)) {
            return true
        }
    }
    return false
}

/**
 * android5.0以上有效
 * 将一个View裁切为圆角或者圆形图案
 * */
fun View?.clipToRound(clipCircle: Boolean = false, radiusPx: Float = 16F) {
    this?.let {
        outlineProvider = CommonRoundOutlineProvider(clipCircle, radiusPx)
        clipToOutline = true
    }
}


// 声动兔强相关扩展代码
// fun Context.toastErrMsg(bean: CommonFailResponseBody?) {
//     bean?.let {
//         val msg = if (it.errMsg.isNullOrBlank()) it.devMsg else it.errMsg
//         var tip = if (msg.isNullOrBlank()) "操作失败,请重试" else msg
//         tip = tip.replace("触发业务流控限制:", "")
//         showToast(tip)
//     }
// }

// fun ImageView.loadFromUrl(url: String?) {
//     ImgLoader.displayImg(context, url, this)
// }

// fun View.setShadowBg(shadowColor: String = "#409EB4E7", cornerRadiusDp: Int = 4, shadowRadiusDp: Int = 6,
//                      shadowOffsetXDp: Int = 0, shadowOffsetYDp: Int = 1) {
//     ShadowDrawable.setShadowDrawable(this,
//             ScreenUtil.dp2px(context, cornerRadiusDp),
//             Color.parseColor(shadowColor),
//             ScreenUtil.dp2px(context, shadowRadiusDp),
//             ScreenUtil.dp2px(context, shadowOffsetXDp),
//             ScreenUtil.dp2px(context, shadowOffsetYDp))
// }

///**
// * 类型转换
// * */
//inline fun <T, reified R> T.convertToObj(doOnError: (T) -> Unit): R? {
//    val jsonStr = when (this) {
//        is HttpException -> { // retrofit2.HttpException
//            this.response().errorBody()?.string()
//        }
//        is String -> this
//        is CharSequence -> this.toString()
//        is R -> return this
//        else -> null
//    }
//    return try {
//        mGson.fromJson<R>(jsonStr, R::class.java)
//    } catch (e: Exception) {
//        e.printStackTrace()
//        doOnError(this)
//        null
//    }
//}