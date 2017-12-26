package org.lynxz.viewpagerdemo

import android.content.Intent
import kotlinx.android.synthetic.main.activity_main.*
import org.lynxz.viewpagerdemo.activity.PermissionDemoActivity
import org.lynxz.viewpagerdemo.base.BaseActivity

class MainActivity : BaseActivity() {
    override fun getLayoutRes() = R.layout.activity_main

    override fun initData() {
    }

    override fun initView() {
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
    }


    override fun afterCreate() {
        tv_permission.setOnClickListener {
            startActivity(Intent(this@MainActivity, PermissionDemoActivity::class.java))
        }
    }
}
