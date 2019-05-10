package org.lynxz.lifecycledemo

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData

object ParaConfig {
    private val msgLiveData = MutableLiveData<String>()

    fun getMsgLiveData(): LiveData<String> = msgLiveData

    fun updateMsg(msg: String, inBgThread: Boolean = false) {
        if (inBgThread) {
            msgLiveData.postValue(msg)
        } else {
            msgLiveData.value = msg
        }
    }
}