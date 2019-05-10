package org.lynxz.lifecycledemo.ui.fragmeng

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_livedata.*
import org.lynxz.lifecycledemo.ParaConfig
import org.lynxz.lifecycledemo.R
import org.lynxz.lifecycledemo.util.Logger

class LiveDataFrag : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_livedata, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ParaConfig.getMsgLiveData().observe(this, Observer<String> { newMsg ->
            tv_msg.text = newMsg
        })

        ParaConfig.getMsgLiveData().observeForever {
            Logger.d("observeForever: $it","tag_livedata")
        }

    }
}