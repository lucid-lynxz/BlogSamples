package org.lynxz.opencvdemo

import android.app.Activity
import android.app.Application
import android.support.annotation.StringRes
import android.support.v4.app.Fragment
import android.text.TextUtils
import android.widget.Toast
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by lynxz on 13/01/2017.
 * 常用扩展函数
 */
fun CharSequence.isEmpty(): Boolean {
    return TextUtils.isEmpty(this)
}

fun Fragment.showToast(msg: String) {
    activity.showToast(msg)
}

fun Fragment.showToast(msgId: Int) {
    activity.showToast(msgId)
}

fun Activity.showToast(msgId: Int) {
    application.showToast(msgId)
}

fun Activity.showToast(msg: String) {
    application.showToast(msg)
}

fun Application?.showToast(msg: String) {
    this?.let {
        Toast.makeText(this.applicationContext, msg, Toast.LENGTH_SHORT).show()
    }
}

fun Application?.showToast(msgId: Int) {
    this?.let {
        Toast.makeText(this.applicationContext, msgId, Toast.LENGTH_SHORT).show()
    }
}

fun Fragment.getStringRes(@StringRes resId: Int): String {
    return this.activity.resources.getString(resId)
}
/**
 * 将当前时间戳转换为指定格式的日期字符串
 * */
fun msec2date(format: String = "yyyy-MM-dd HH:mm:ss") = SimpleDateFormat(format).format(Date(System.currentTimeMillis()))

/**
 * 保留两位小数,并返回字符串
 * */
fun Double.yuan(): String = DecimalFormat("0.##").format(this)

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