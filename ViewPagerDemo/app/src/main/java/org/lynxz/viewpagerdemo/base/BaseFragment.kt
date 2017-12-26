package org.lynxz.viewpagerdemo.base

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.lynxz.viewpagerdemo.Logger

/**
 * Created by lynxz on 26/12/2017.
 */
abstract class BaseFragment : Fragment() {
    abstract fun getLayoutRes(): Int
    abstract fun initView()
    abstract fun afterCreated()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(getLayoutRes(), container, false)
    }


    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Logger.d("${javaClass.simpleName} ${hashCode()}")
        initView()
        afterCreated()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Logger.d("${javaClass.simpleName} ${hashCode()}")
    }
}