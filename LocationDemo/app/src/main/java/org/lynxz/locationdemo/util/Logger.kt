package org.lynxz.locationdemo.util

import android.util.Log
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

/**
 * Created by lynxz on 31/01/2017.
 * V1.0
 * 默认不可打印,请通过设置 Logger.init(logLevel,Tag) 来设定可打印等级
 * 格式化打印日志,若是需要打印json,可使用 Logger.json(jsonStr),或者 Logger.json(tag,jsonStr)
 * Logger.i(tag,msg) 或 Logger.i(msg) 使用通用的tag值
 */
object Logger {

    val verboseLevel = 0 //打印所有日志
    val debugLevel = 1
    val infoLevel = 2
    val warnLevel = 3
    val errorLevel = 4
    val noneLevel = 10 // 不打印任何级别日志

    val JSON_INDENT = 2
    val MIN_STACK_OFFSET = 3

    private var TAG = "custom_logger"
    var logLevel = debugLevel // 需要打印的日志等级(大于等于该等级的日志会被打印)

    @JvmStatic
    fun init(level: Int, clazz: Class<*>) {
        TAG = clazz.simpleName
        logLevel = level
    }

    /**
     * 支持用户自己传tag，可扩展性更好
     */
    @JvmStatic
    fun init(level: Int, tag: String) {
        TAG = tag
        logLevel = level
    }

    @JvmStatic
    fun d(msg: String) {
        d(TAG, msg)
    }

    @JvmStatic
    fun i(msg: String) {
        i(TAG, msg)
    }

    @JvmStatic
    fun w(msg: String) {
        w(TAG, msg)
    }

    @JvmStatic
    fun e(msg: String) {
        e(TAG, msg)
    }

    @JvmStatic
    fun e(tag: String, msg: String) {
        if (errorLevel >= logLevel) {
            if (msg.isNotBlank()) {
                val s = getMethodNames()
                Log.e(tag, String.format(s, msg))
            }
        }
    }

    @JvmStatic
    fun w(tag: String, msg: String) {
        if (warnLevel >= logLevel) {
            if (msg.isNotBlank()) {
                val s = getMethodNames()
                Log.w(tag, String.format(s, msg))
            }
        }
    }

    @JvmStatic
    fun i(tag: String, msg: String) {
        if (infoLevel >= logLevel) {
            if (msg.isNotBlank()) {
                val s = getMethodNames()
                Log.i(tag, String.format(s, msg))
            }
        }
    }

    @JvmStatic
    fun d(tag: String, msg: String) {
        if (debugLevel >= logLevel) {
            if (msg.isNotBlank()) {
                val s = getMethodNames()
                Log.d(tag, String.format(s, msg))
            }
        }
    }

    /**
     * 打印json格式化字符串,在log过滤条中使用关键字 "system.out" 来搜索查找
     * @param tag 当打印或解析出错时,打印日志用
     * */
    @JvmStatic
    fun json(tag: String, json: String) {
        var jsonT = json

        if (jsonT.isBlank()) {
            d("Empty/Null json content", tag)
            return
        }

        try {
            jsonT = jsonT.trim { it <= ' ' }
            if (jsonT.startsWith("{")) {
                val jsonObject = JSONObject(jsonT)
                var message = jsonObject.toString(JSON_INDENT)
                message = message.replace("\n".toRegex(), "\n║ ")
                val s = getMethodNames()
                println(String.format(s, message))
                return
            }
            if (jsonT.startsWith("[")) {
                val jsonArray = JSONArray(jsonT)
                var message = jsonArray.toString(JSON_INDENT)
                message = message.replace("\n".toRegex(), "\n║ ")
                val s = getMethodNames()
                println(String.format(s, message))
                return
            }
            e("Invalid Json", tag)
        } catch (e: JSONException) {
            e("Invalid Json", tag)
        }
    }

    /**
     * 获取程序执行的线程名,类名和方法名,以及行号等信息
     * */
    private fun getMethodNames(): String {
        val sElements = Thread.currentThread().stackTrace
        var stackOffset = getStackOffset(sElements)
        stackOffset++
        val builder = StringBuilder()

        //builder.append(Thread.currentThread().name).append(" ")
        builder.append(sElements[stackOffset].methodName)
            .append("(").append(sElements[stackOffset].fileName)
            .append(":").append(sElements[stackOffset].lineNumber)
            .append(") ").append("%s")
        return builder.toString()
    }

    fun getStackOffset(trace: Array<StackTraceElement>): Int {
        var i = MIN_STACK_OFFSET
        while (i < trace.size) {
            val e = trace[i]
            val name = e.className
            if (name != Logger::class.java.name) {
                return --i
            }
            i++
        }
        return -1
    }
}