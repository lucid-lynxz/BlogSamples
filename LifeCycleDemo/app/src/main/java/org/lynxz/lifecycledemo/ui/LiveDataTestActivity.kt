package org.lynxz.lifecycledemo.ui

import android.os.Bundle
import android.support.v4.app.SupportActivity
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_livedata.*
import org.lynxz.lifecycledemo.ParaConfig
import org.lynxz.lifecycledemo.R
import org.lynxz.lifecycledemo.ui.fragmeng.LiveDataFrag


class LiveDataTestActivity : AppCompatActivity() {
    private val mFrag = LiveDataFrag()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_livedata)

        btn_add_frag.setOnClickListener {
            supportFragmentManager
                .beginTransaction()
                .add(R.id.ll_container, mFrag)
                .commit()
            it.isEnabled = false
            btn_remove_frag.isEnabled = true
        }

        btn_remove_frag.setOnClickListener {
            supportFragmentManager
                .beginTransaction()
                .remove(mFrag)
                .commit()
            btn_add_frag.isEnabled = true
            it.isEnabled = false
        }

        btn_get_info.setOnClickListener {
            ParaConfig.getMsgLiveData().let {
                tv_info.text = "hasActiveObservers=${it.hasActiveObservers()},hasObservers=${it.hasObservers()} "
            }

        }

        btn_update_msg.setOnClickListener {
            ParaConfig.updateMsg(edt_msg.text.toString())
        }

        btn_update_msg_in_thread.setOnClickListener {
            Thread {
                ParaConfig.updateMsg(edt_msg.text.toString(), true)
            }.start()
        }

        btn_update_msg_twice.setOnClickListener {
            ParaConfig.updateMsg("msgFromPostValue", true)
            ParaConfig.updateMsg("msgFromSetValue", false)
        }
    }
}