package org.lynxz.explandablelistviewdemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //偷下懒,直接从项目中抠过来
                startActivity(new Intent(MainActivity.this, BasicExpandableListView.class));
            }
        });

        findViewById(R.id.btn1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //这个是别人写的页面,研究一下,原文: http://blog.csdn.net/h7870181/article/details/40400155
                startActivity(new Intent(MainActivity.this, PinnedHeaderActivity.class));
            }
        });

        findViewById(R.id.btn2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //https://github.com/idunnololz/AnimatedExpandableListView
                startActivity(new Intent(MainActivity.this, AnimatedActivity.class));
            }
        });

        findViewById(R.id.btn3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //https://github.com/idunnololz/AnimatedExpandableListView
                startActivity(new Intent(MainActivity.this, ThreeLevelActivity.class));
            }
        });
    }
}
