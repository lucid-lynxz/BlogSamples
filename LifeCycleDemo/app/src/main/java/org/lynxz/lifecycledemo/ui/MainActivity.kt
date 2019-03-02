package org.lynxz.lifecycledemo.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import org.lynxz.lifecycledemo.R
import org.lynxz.lifecycledemo.observer.MainActObserver

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        lifecycle.addObserver(MainActObserver())
    }
}
