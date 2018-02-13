package org.lynxz.aidldemo

import android.annotation.SuppressLint
import android.app.Service
import android.content.Intent
import android.os.*
import android.util.Log

/**
 * Created by lynxz on 13/02/2018.
 * 博客: https://juejin.im/user/5812c2b0570c3500605a15ff
 */
class DemandService : Service() {
    val TAG = "DemandService"
    val MSG_SEND = 100

    override fun onBind(intent: Intent?): IBinder {
        mHandler.sendEmptyMessageDelayed(MSG_SEND, 3000)
        return mDemandManager
    }

    override fun onUnbind(intent: Intent?): Boolean {
        mHandler.removeMessages(MSG_SEND)
        return super.onUnbind(intent)
    }

    @SuppressLint("HandlerLeak")
    private val mHandler = object : Handler() {
        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)

            // 通过 beginBroadcast() 获取 RemoteCallbackList 元素个数
            val count = mRemoteCallbackList.beginBroadcast()
            var msgIndex = 0
            for (i in 0 until count) {
                try {
                    mRemoteCallbackList.getBroadcastItem(i).onReceiveMessage(MessageBean("server msg ${System.currentTimeMillis()}", msgIndex++))
                } catch (e: RemoteException) {
                    e.printStackTrace()
                }
            }
            // 注意:beginBroadcast() 需与 finishBroadcast() 配套使用
            mRemoteCallbackList.finishBroadcast()
            this.sendEmptyMessageDelayed(MSG_SEND, 3000)
        }
    }


    val mRemoteCallbackList: RemoteCallbackList<IListener> = RemoteCallbackList()

    private val mDemandManager = object : IDemandManager.Stub() {
        @Throws(RemoteException::class)
        override fun getDemand(): MessageBean {
            return MessageBean("hello", 1)
        }

        override fun setDemandIn(bean: MessageBean?) {
            Log.d(TAG, "setDemandIn ${bean?.toString()}")
            setDemandOut(MessageBean("hello from server", 49))
        }

        override fun setDemandOut(bean: MessageBean?) {
            Log.d(TAG, "setDemandOut ${bean?.toString()}")
            bean?.apply {
                content = "setDemandOut content"
                level = 3
            }
        }

        override fun setDemandInOut(bean: MessageBean?) {
            Log.d(TAG, "setDemandInOut ${bean?.toString()}")
            bean?.apply {
                content = "setDemandInOut content"
                level = 5
            }
        }

        override fun registerListener(listener: IListener?) {
            mRemoteCallbackList.register(listener)
        }

        override fun unregisterListener(listener: IListener?) {
            mRemoteCallbackList.unregister(listener)
        }
    }
}