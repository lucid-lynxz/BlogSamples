package org.lynxz.locationdemo

import android.Manifest
import android.location.Location
import android.location.LocationListener
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import org.lynxz.locationdemo.fragment.BaseTransFragment
import org.lynxz.locationdemo.fragment.IPermissionCallback
import org.lynxz.locationdemo.fragment.PermissionFragment
import org.lynxz.locationdemo.fragment.PermissionResultInfo
import org.lynxz.locationdemo.util.LocationUtil
import org.lynxz.locationdemo.util.Logger

class MainActivity : AppCompatActivity() {

    private val mLocationUtil by lazy {
        LocationUtil.getInstance(this).apply {
            setLocationListener(object : LocationListener {
                override fun onLocationChanged(location: Location?) {
                    location?.let {
                        tv_info.text = "定位回调: ${location.longitude},${location.latitude}"
                    }
                }

                override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
                }

                override fun onProviderEnabled(provider: String?) {
                }

                override fun onProviderDisabled(provider: String?) {
                }

            })
        }
    }

    private val permissionFrag by lazy {
        BaseTransFragment.getTransFragment(this@MainActivity, "permission_tag", PermissionFragment())
    }

    private val permissionCallback = object : IPermissionCallback {

        override fun onRequestResult(permission: PermissionResultInfo) {
            // 具体某个权限的授权结果
            Logger.d("授权结果\n权限名=${permission.name},是否授权=${permission.granted},是否可再弹出系统权限框=${permission.shouldShowRequestPermissionRationale}")
            if (!permission.granted) {
                showToast("定位权限申请未通过,请重试")
                return
            }

            mLocationUtil.checkAndOpenGpsProvider().yes {
                val lastLoc = mLocationUtil.lastLocation
                if (lastLoc == null) {
                    tv_info.text = "获取定位信息失败"
                } else {
                    tv_info.text = "lastKnownLocation: ${lastLoc.longitude},${lastLoc.latitude}"
                }
            }
        }

        override fun onAllRequestResult(allGranted: Boolean) {
            // 所申请的权限是否全部都通过了

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_get_loc.setOnClickListener {
            permissionFrag?.requestPermission(Manifest.permission.ACCESS_FINE_LOCATION, permissionCallback)
        }
    }
}