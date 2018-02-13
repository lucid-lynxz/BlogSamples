package org.lynxz.aidlclientdemo

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.support.v7.app.AppCompatActivity
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*
import org.lynxz.aidldemo.IDemandManager
import org.lynxz.aidldemo.IListener
import org.lynxz.aidldemo.MessageBean


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_send.setOnClickListener {
            val input = edt_input.text.toString()
            if (input.isBlank()) {
                showToast("请输入内容")
            } else {
                demandManager?.setDemandIn(MessageBean("client: $input", 38))
            }
        }

        val intent = Intent()
        intent.action = "org.lynxz.aidl"//service的action
        intent.`package` = "org.lynxz.aidldemo"//aidl文件夹里面aidl文件的包名
        bindService(intent, connection, Context.BIND_AUTO_CREATE)
    }

    private var demandManager: IDemandManager? = null
    val connection = object : ServiceConnection {
        override fun onServiceDisconnected(name: ComponentName?) {
            demandManager?.unregisterListener(listener)
            demandManager = null
        }

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            // 获取该对象后即可进行通讯
            Log.d("clientAidl", "onServiceConnected $name")
            demandManager = IDemandManager.Stub.asInterface(service)
            demandManager?.registerListener(listener)
        }
    }


    // 接收来自server端的消息
    private val listener = object : IListener.Stub() {
        override fun onReceiveMessage(bean: MessageBean?) {
            //该方法运行在 Binder 线程池中，非ui线程
            Log.d("clientAidl", "onReceiveMessage $bean")
            tv_receive.post {
                tv_receive.text = bean?.toString()
            }
        }
    }

    override fun onStop() {
        super.onStop()
        unbindService(connection)
    }
}
