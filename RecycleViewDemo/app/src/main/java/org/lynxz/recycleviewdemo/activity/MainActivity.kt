package org.lynxz.recycleviewdemo.activity

import android.content.Intent
import android.content.pm.PackageManager
import kotlinx.android.synthetic.main.activity_main.*
import org.lynxz.recycleviewdemo.Logger
import org.lynxz.recycleviewdemo.R


class MainActivity : BaseActivity() {
    override fun getLayoutRes() = R.layout.activity_main

    override fun afterCreate() {
        // 分组粘性标题
        btn_title.setOnClickListener { startActivity(Intent(this@MainActivity, GroupTitleRvActivity::class.java)) }

        // todo 折叠展开
        // https://juejin.im/entry/5a6df3c76fb9a01c9e463468?utm_source=gold_browser_extension



        getReleaseTime()
    }

    private fun getReleaseTime(): String {
        var releaseTime = ""
        try {
            releaseTime = packageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA).metaData.getString("releaseTime")
            Logger.d("releaseTime = $releaseTime")
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return releaseTime
    }
}
