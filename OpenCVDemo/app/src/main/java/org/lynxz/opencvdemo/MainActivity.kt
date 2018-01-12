package org.lynxz.opencvdemo

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.lynxz.opencvdemo.facedetect.FdActivity
import org.opencv.core.Core

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val start = System.currentTimeMillis()
        iv_opencv.setImageBitmap(ImageProcessUtils.blur(this, R.mipmap.honglian))
        iv_opencv.setOnClickListener {
            if (Build.VERSION.SDK_INT >= 23) {
                if (checkSelfPermission(android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    startActivity(Intent(this, FdActivity::class.java))
                } else {
                    requestPermissions(arrayOf(Manifest.permission.CAMERA), 100)
                }
            }
        }

        Logger.d("OpenCV ${Core.VERSION} 耗时: ${System.currentTimeMillis() - start} 毫秒")
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 100 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startActivity(Intent(this, FdActivity::class.java))
        }
    }
}
