
import android.content.Context
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * Created by lynxz on 06/07/2017.
 * 用于 sharePreference(简称sp) 属性代理
 * 使用方法:
 * var isLogin by SpDelegateUtil(this,"userId", "")
 * isLogin = true // 会直接保存到sp中
 */
class SpDelegateUtil<T>(val act: Context, val keyName: String, val defaultValue: T, val spName: String = "sp_para") : ReadWriteProperty<Any?, T> {
    @Suppress("CAST_NEVER_SUCCEEDS")
    private val sp by lazy {
        act.applicationContext.getSharedPreferences(spName, Context.MODE_PRIVATE)
    }

    override fun getValue(thisRef: Any?, property: KProperty<*>): T = findPreference(keyName, defaultValue)

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        putPreference(keyName, value)
    }

    @Suppress("UNCHECKED_CAST")
    private fun <U> findPreference(name: String, default: U): U = with(sp) {
        val res: Any = when (default) {
            is Long -> getLong(name, default)
            is String -> getString(name, default)
            is Int -> getInt(name, default)
            is Boolean -> getBoolean(name, default)
            is Float -> getFloat(name, default)
            else -> throw IllegalArgumentException("This type can not be saved")
        }
        res as U
    }

    private fun <U> putPreference(name: String, value: U) = with(sp.edit()) {
        when (value) {
            is Long -> putLong(name, value)
            is String -> putString(name, value)
            is Int -> putInt(name, value)
            is Boolean -> putBoolean(name, value)
            is Float -> putFloat(name, value)
            else -> throw IllegalArgumentException("This type can not be saved")
        }.apply()
    }
}