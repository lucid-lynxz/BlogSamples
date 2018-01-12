package org.lynxz.opencvdemo

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory

import org.opencv.osgi.OpenCVNativeLoader

object ImageProcessUtils {

    /**
     * 毛玻璃一张图片
     *
     * @param srcBitmap 原始图片
     * @return 毛玻璃后的图片
     */
    fun blur(srcBitmap: Bitmap): Bitmap {
        // 获取原始图片的宽高
        val width = srcBitmap.width
        val height = srcBitmap.height
        // 初始化一个用来存储图片所有像素的int数组
        val pixels = IntArray(width * height)
        // 把原始图片的所有原始存入数组中
        srcBitmap.getPixels(pixels, 0, width, 0, 0, width, height)
        // 通过jni本地方法毛玻璃化图片
        blurImage(pixels, width, height)
        // 创建一个新的图片
        val newBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
        // 把处理后的图片像素设置给新图片
        newBitmap.setPixels(pixels, 0, width, 0, 0, width, height)
        return newBitmap
    }

    fun blur(cxt: Context, imgId: Int) = blur(getResImg(cxt, imgId))

    private fun getResImg(cxt: Context, imgId: Int): Bitmap {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true//只读取参数,不分配内存
        BitmapFactory.decodeResource(cxt.resources, imgId, options)
//        val imageWidth = options.outWidth
//        val imageHeight = options.outHeight
//        val outMimeType = options.outMimeType

        //缩放比例
        options.inSampleSize = 2
        options.inJustDecodeBounds = false

        //实际解析图片资源
        return BitmapFactory.decodeResource(cxt.resources, imgId, options)
    }

    // 毛玻璃图片
    external fun blurImage(pixels: IntArray, w: Int, h: Int)

    // 加载so库
    init {
        System.loadLibrary("image_process")
        OpenCVNativeLoader().init()
    }
}