package org.lynxz.viewpagerdemo.activity

import kotlinx.android.synthetic.main.activity_permission_demo.*
import org.lynxz.viewpagerdemo.Logger
import org.lynxz.viewpagerdemo.R
import org.lynxz.viewpagerdemo.adapter.MyFragmentStatePagerAdapter
import org.lynxz.viewpagerdemo.base.BaseActivity
import org.lynxz.viewpagerdemo.base.BaseFragment
import org.lynxz.viewpagerdemo.config.LogTag
import org.lynxz.viewpagerdemo.fragment.FragmentOne
import org.lynxz.viewpagerdemo.fragment.FragmentTwo

/**
 * Created by lynxz on 26/12/2017.
 * 测试运行期时间禁用权限后的一些现象
 */
class PermissionDemoActivity : BaseActivity() {
    lateinit var fragOne: FragmentOne
    lateinit var fragTwo: FragmentTwo
    var fragList = ArrayList<BaseFragment>()

    override fun getLayoutRes() = R.layout.activity_permission_demo

    override fun initData() {
        Logger.init(Logger.debugLevel, LogTag.TAG_PERMISSION_TEST)
        fragOne = FragmentOne()
        fragTwo = FragmentTwo()
        fragList.apply {
            add(fragOne)
            add(fragTwo)
        }
    }

    override fun initView() {
        vp_permission.apply {
            adapter = object : MyFragmentStatePagerAdapter(supportFragmentManager) {
                override fun getItem(position: Int) = fragList[position]
                override fun getCount() = fragList.size
            }
            offscreenPageLimit = 1
        }
    }

    override fun afterCreate() {
        Logger.d("${fragOne.hashCode()} -- ${fragTwo.hashCode()}")
    }

    override fun onResume() {
        super.onResume()
        Logger.d("${fragOne.hashCode()} ${fragOne.isAdded}")
    }

}