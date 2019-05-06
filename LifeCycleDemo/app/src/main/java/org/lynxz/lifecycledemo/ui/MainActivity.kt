package org.lynxz.lifecycledemo.ui

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.lynxz.lifecycledemo.R
import org.lynxz.lifecycledemo.observer.MainActObserver
import org.lynxz.lifecycledemo.observer.MainActObserverSub

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        lifecycle.addObserver(MainActObserver())
//        lifecycle.addObserver(MainActObserverSub())

        btn_second.setOnClickListener { startActivity(Intent(this@MainActivity, SecondActivity::class.java)) }
    }
}
