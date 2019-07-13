package org.lynxz.locationdemo.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v4.app.FragmentManager

/**
 * Created by lynxz on 2019/1/5
 * E-mail: lynxz8866@gmail.com
 * 参考文章: https://juejin.im/post/5c2f0a0951882524661d1252
 * V1.1
 * Description: 透明fragment,子类用于封装实现一些长逻辑,并对外暴露调用方法
 * 使用方法:
 * 1. 子类fragment不可见,实现各种业务逻辑,并对外暴露调用方法
 * 2. 在activity中通过调用 [getTransFragment] 来将子类fragment添加到activity中,若返回null表示注入失败;
 */
abstract class BaseTransFragment : Fragment() {

    protected lateinit var hostActivity: FragmentActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    companion object {
        @JvmStatic
        fun <T : BaseTransFragment> getTransFragment(
            fragmentActivity: FragmentActivity, fragmentTag: String,
            newBusinessTranFragInstance: T
        ): T? {
            try {
                val fragmentManager = fragmentActivity.supportFragmentManager
                var fragment = findFragment<T>(fragmentManager, fragmentTag)

                if (fragment == null) {
                    fragment = newBusinessTranFragInstance
                    fragmentManager.beginTransaction()
                        .add(fragment, fragmentTag)
                        .commitAllowingStateLoss() //避免数据保存和恢复导致的crash
                    fragmentManager.executePendingTransactions()
                }
                return fragment.apply {
                    hostActivity = fragmentActivity
                }
            } catch (e: Exception) {
                e.printStackTrace()
                return null
            }
        }

        private fun <T : BaseTransFragment> findFragment(fragmentManager: FragmentManager, fragmentTag: String): T? {
            val tagFragment = fragmentManager.findFragmentByTag(fragmentTag)
            return if (tagFragment == null) {
                null
            } else {
                tagFragment as T
            }
        }
    }
}