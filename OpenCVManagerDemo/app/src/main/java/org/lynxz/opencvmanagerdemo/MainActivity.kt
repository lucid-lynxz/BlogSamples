package org.lynxz.opencvmanagerdemo

import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Handler
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
import kotlin.concurrent.thread


class MainActivity : AppCompatActivity() {

    private val mHandler = Handler()
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

        btn_blur.setOnClickListener { blur(TYPE_MEAN_BLUR) }
        btn_gaussian_blur.setOnClickListener { blur(TYPE_GAUSSIAN_BLUR) }
        btn_medium_blur.setOnClickListener { blur(TYPE_MEDIAN_BLUR, R.mipmap.jyzs) }
    }

    val TYPE_MEAN_BLUR = 1
    val TYPE_MEDIAN_BLUR = 2
    val TYPE_GAUSSIAN_BLUR = 3
    val TYPE_SHARPEN = 4
    val TYPE_DILATE = 5
    val TYPE_ERODE = 6
    val TYPE_THRESHOLD = 7
    val TYPE_ADAPTIVE_THRESHOLD = 8

    private fun blur(type: Int, imgId: Int = R.mipmap.honglian) {
        if (!openCvManagerInstalled) {
            showToast("请先安装OpenCVManager.apk")
        } else {
            iv_ori.setImageResource(imgId)
            thread {
                // 读取图片文件
                var srcImg = BitmapFactory.decodeResource(resources, imgId)
                // 创建Mat对象,并将图片数据存储到Mat中
                val srcMat = Mat(srcImg.height, srcImg.width, CvType.CV_8UC4)
                Utils.bitmapToMat(srcImg, srcMat)
                // 进行模糊操作
                when (type) {
                    TYPE_MEAN_BLUR -> {
                        Imgproc.blur(srcMat, srcMat, Size(30.0, 30.0))
                    }
                    TYPE_GAUSSIAN_BLUR -> {
                        // 要求 Size 宽高为奇数: ksize.width > 0 && ksize.width % 2 == 1 && ksize.height > 0 && ksize.height % 2 == 1
                        Imgproc.GaussianBlur(srcMat, srcMat, Size(3.0, 3.0), 2.0)
                    }
                    TYPE_MEDIAN_BLUR -> {
                        // 中值模糊,一般用于去除椒盐噪声
                        Imgproc.medianBlur(srcMat, srcMat, 5)
                    }
                }
                // 将Mat对象还原成图片文件
                Utils.matToBitmap(srcMat, srcImg)
                runOnUiThread {
                    iv_after.setImageBitmap(srcImg)
                }
            }
        }
    }
}
