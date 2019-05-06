package org.lynxz.lifecycledemo.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import org.lynxz.lifecycledemo.R
import org.lynxz.lifecycledemo.observer.SecondActObserver

class SecondActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)
        lifecycle.addObserver(SecondActObserver())
    }
}