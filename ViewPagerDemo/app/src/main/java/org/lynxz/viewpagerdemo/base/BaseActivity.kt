package org.lynxz.viewpagerdemo.base

import android.os.Bundle
import android.support.v7.app.AppCompatActivity

/**
 * Created by lynxz on 26/12/2017.
 */
abstract class BaseActivity : AppCompatActivity() {

    abstract fun getLayoutRes(): Int
    abstract fun initData()
    abstract fun initView()
    abstract fun afterCreate()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutRes())
        initData()
        initView()
        afterCreate()
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}