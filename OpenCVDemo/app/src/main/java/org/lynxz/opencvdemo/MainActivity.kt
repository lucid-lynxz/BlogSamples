package org.lynxz.opencvdemo

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.opencv.core.Core
import org.opencv.core.CvType
import org.opencv.core.Mat
import org.opencv.core.Scalar

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        iv_opencv.setImageBitmap(ImageProcessUtils.blur(this, R.mipmap.honglian))

        showToast("OpenCV ${Core.VERSION}")

        // rows -> height, cols -> width 生成一个代表10*5像素图像的矩阵
        val m = Mat(5, 10, CvType.CV_8UC1, Scalar(0.0))


//        val imread = Imgcodecs.imread("")
    }
}
