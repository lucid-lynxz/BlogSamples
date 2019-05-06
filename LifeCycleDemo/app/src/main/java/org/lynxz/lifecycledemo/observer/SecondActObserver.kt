package org.lynxz.lifecycledemo.observer

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import org.lynxz.lifecycledemo.util.Logger

/**
 * Created by lynxz on 2019/3/2
 * E-mail: lynxz8866@gmail.com
 *
 * Description:
 */
class SecondActObserver : LifecycleObserver {
//    override fun onStateChanged(source: LifecycleOwner?, event: Lifecycle.Event?) {
//        Logger.d("MainActObserver onStateChanged $event")
//    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate() {
        Logger.d("SecondActObserver $this onCreate")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onStart() {
        Logger.d("SecondActObserver onStart")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {
        Logger.d("SecondActObserver onResume")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPause() {
        Logger.d("SecondActObserver onPause")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onStop() {
        Logger.d("SecondActObserver onStop")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        Logger.d("SecondActObserver onDestroy")
    }
}
