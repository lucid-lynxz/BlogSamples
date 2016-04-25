package org.lynxz.scrollwebview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import org.lynxz.webviewdemo.R;

public class ScrollActivity extends AppCompatActivity {

    private MyWebView mWv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scroll);

        initView();
    }

    private void initView() {
        mWv = (MyWebView) findViewById(R.id.wv);
        mWv.loadUrl("file:///android_asset/scrollTest.html");
    }
}
