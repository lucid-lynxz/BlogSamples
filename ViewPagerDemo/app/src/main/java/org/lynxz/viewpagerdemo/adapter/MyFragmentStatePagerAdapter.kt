package org.lynxz.viewpagerdemo.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.view.ViewGroup
import org.lynxz.viewpagerdemo.Logger
import org.lynxz.viewpagerdemo.base.BaseFragment

/**
 * Created by lynxz on 26/12/2017.
 */
class MyFragmentStatePagerAdapter(fm: FragmentManager, var data: ArrayList<BaseFragment> = arrayListOf()) : FragmentStatePagerAdapter(fm) {

    var currentFragment: Fragment? = null

    override fun setPrimaryItem(container: ViewGroup?, position: Int, obj: Any?) {
        super.setPrimaryItem(container, position, obj)
        currentFragment = obj as Fragment?
        Logger.d("setPrimaryItem $position ${obj?.hashCode()}")
        if (obj is BaseFragment) {
            if (getItem(position) !== obj) {
                data[position] = obj
            }
        }
    }

    override fun getCount() = data.size
    override fun getItem(position: Int) = data[position]
}