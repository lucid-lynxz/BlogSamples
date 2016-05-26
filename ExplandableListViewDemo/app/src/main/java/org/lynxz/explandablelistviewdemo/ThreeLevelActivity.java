package org.lynxz.explandablelistviewdemo;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ExpandableListView;

import org.lynxz.explandablelistviewdemo.adapter.ElParentAdapter;

import java.util.ArrayList;
import java.util.List;

public class ThreeLevelActivity extends AppCompatActivity {

    private ExpandableListView mElParent;
    private ArrayList<String> mParentGroupList;
    private ArrayList<List<String>> mChildGroupList;
    private ArrayList<String> mChildItemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_three_level);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        initData();
        initView();
    }

    private void initData() {
        mParentGroupList = new ArrayList<>();
        mChildGroupList = new ArrayList<>();
        mChildItemList = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            mParentGroupList.add("parent - " + i);
        }

        for (int i = 0; i < mParentGroupList.size(); i++) {
            ArrayList<String> child = new ArrayList<>();
            for (int j = 0; j < 5; j++) {
                child.add("parentChild " + (i * 10 + j));
            }
            mChildGroupList.add(child);
        }
    }


    private void initView() {
        mElParent = findView(R.id.el_parent);
        mElParent.setAdapter(new ElParentAdapter(this, mParentGroupList, mChildGroupList));
    }

    public <T extends View> T findView(int id) {
        return (T) findViewById(id);
    }
}
