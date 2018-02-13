package org.lynxz.aidldemo

import android.content.ComponentName
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.support.v7.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private var mIRemoteService = null
    // 在 client 端调用我们的service接口
    private val mConnection: ServiceConnection = object : ServiceConnection {
        // Called when the connection with the service is established
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            // 转换为server端的service
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            mIRemoteService = null
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
