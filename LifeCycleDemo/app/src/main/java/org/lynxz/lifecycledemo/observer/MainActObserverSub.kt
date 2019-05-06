package org.lynxz.lifecycledemo.observer

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.OnLifecycleEvent
import org.lynxz.lifecycledemo.util.Logger

class MainActObserverSub : MainActObserver() {
    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResumeSub() {
        Logger.d("MainActObserverSub onResume")
    }

    // @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    override fun onPause() {
        Logger.d("MainActObserverSub onPause")
    }
}