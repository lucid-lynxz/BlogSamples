package org.lynxz.opencvdemo

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
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


//        var resImg = getResImg(this, R.mipmap.honglian)
//        val srcMat = Mat(resImg.height, resImg.width, CvType.CV_8UC4)
//        Imgproc.blur(srcMat, srcMat, Size(30.0, 30.0))
//        Utils.matToBitmap(srcMat, resImg)
//        iv_opencv.setImageBitmap(resImg)

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

    fun getResImg(cxt: Context, imgId: Int): Bitmap {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true//只读取参数,不分配内存
        BitmapFactory.decodeResource(cxt.resources, imgId, options)

        //缩放比例
        options.inSampleSize = 2
        options.inJustDecodeBounds = false

        //实际解析图片资源
        return BitmapFactory.decodeResource(cxt.resources, imgId, options)
    }
}
