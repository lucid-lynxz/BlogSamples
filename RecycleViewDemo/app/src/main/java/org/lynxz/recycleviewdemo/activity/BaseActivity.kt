package org.lynxz.recycleviewdemo.activity

import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.v7.app.AppCompatActivity

/**
 * Created by lynxz on 02/02/2018.
 * 博客: https://juejin.im/user/5812c2b0570c3500605a15ff
 */
abstract class BaseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutRes())
        afterCreate()
    }

    /**
     * 获取布局id
     * */
    @LayoutRes
    abstract fun getLayoutRes(): Int

    abstract fun afterCreate()
}