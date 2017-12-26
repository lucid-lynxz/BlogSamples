package org.lynxz.viewpagerdemo.fragment

import android.Manifest
import com.tbruyelle.rxpermissions2.RxPermissions
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.fragment_common.*
import org.lynxz.viewpagerdemo.R
import org.lynxz.viewpagerdemo.base.BaseFragment
import org.lynxz.viewpagerdemo.showToast

/**
 * Created by lynxz on 26/12/2017.
 */
class FragmentOne : BaseFragment() {
    override fun getLayoutRes() = R.layout.fragment_common

    override fun initView() {
        tv_common.text = javaClass.simpleName
    }

    override fun afterCreated() {
        RxPermissions(activity)
                .request(Manifest.permission.RECORD_AUDIO)
                .subscribe(object : Observer<Boolean> {
                    override fun onSubscribe(d: Disposable) {
                    }

                    override fun onNext(granted: Boolean) {
                        showToast("获取RECORD_AUDIO权限结果: $granted")
                    }

                    override fun onComplete() {
                    }

                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                    }
                })
    }
}