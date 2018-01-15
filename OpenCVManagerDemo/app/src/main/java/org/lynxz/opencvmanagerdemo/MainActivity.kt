package org.lynxz.opencvmanagerdemo

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.opencv.android.BaseLoaderCallback
import org.opencv.android.LoaderCallbackInterface
import org.opencv.android.OpenCVLoader
import org.opencv.android.Utils
import org.opencv.core.CvType
import org.opencv.core.Mat
import org.opencv.core.Size
import org.opencv.imgproc.Imgproc


class MainActivity : AppCompatActivity() {

    private var openCvManagerInstalled = false
    private val openCvCallback = object : BaseLoaderCallback(this) {
        override fun onManagerConnected(status: Int) {
            super.onManagerConnected(status)
            showToast("openCvManager apk 安装状态: ${status == LoaderCallbackInterface.SUCCESS}")
            when (status) {
                LoaderCallbackInterface.SUCCESS -> {
                    // opencv manager 已安装
                    openCvManagerInstalled = true
                }
                else -> {
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_4_0, this, openCvCallback)

        btn_blur.setOnClickListener {
            if (!openCvManagerInstalled) {
                showToast("请先安装OpenCVManager.apk")
            } else {
                // 读取图片文件
                var srcImg = BitmapFactory.decodeResource(resources, R.mipmap.honglian)
                // 创建Mat对象,并将图片数据存储到Mat中
                val srcMat = Mat(srcImg.height, srcImg.width, CvType.CV_8UC4)
                Utils.bitmapToMat(srcImg, srcMat)
                // 进行模糊操作
                Imgproc.blur(srcMat, srcMat, Size(10.0, 10.0))
                // 将Mat对象还原成图片文件
                Utils.matToBitmap(srcMat, srcImg)

                iv_after.setImageBitmap(srcImg)
            }
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
