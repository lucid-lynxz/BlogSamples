package org.lynxz.explandablelistviewdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ExpandableListView;

import org.lynxz.explandablelistviewdemo.adapter.PinnedHeaderExpandableAdapter;
import org.lynxz.explandablelistviewdemo.widget.PinnedHeaderExpandableListView;

public class PinnedHeaderActivity extends AppCompatActivity {

    private PinnedHeaderExpandableListView explistview;
    private String[][] childrenData = new String[10][10];
    private String[] groupData = new String[10];
    private int expandFlag = -1;//控制列表的展开
    private PinnedHeaderExpandableAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pinned_header);


        initView();
        initData();
    }

    /**
     * 初始化VIEW
     */
    private void initView() {
        explistview = (PinnedHeaderExpandableListView) findViewById(R.id.explistview);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        for (int i = 0; i < 10; i++) {
            groupData[i] = "分组" + i;
        }

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                childrenData[i][j] = "好友" + i + "-" + j;
            }
        }
        //设置悬浮头部VIEW
        explistview.setHeaderView(getLayoutInflater().inflate(R.layout.group_head,
                explistview, false));
        adapter = new PinnedHeaderExpandableAdapter(childrenData, groupData, getApplicationContext(), explistview);
        explistview.setAdapter(adapter);

        //设置单个分组展开
        //explistview.setOnGroupClickListener(new GroupClickListener());
    }

    class GroupClickListener implements ExpandableListView.OnGroupClickListener {
        @Override
        public boolean onGroupClick(ExpandableListView parent, View v,
                                    int groupPosition, long id) {
            if (expandFlag == -1) {
                // 展开被选的group
                explistview.expandGroup(groupPosition);
                // 设置被选中的group置于顶端
                explistview.setSelectedGroup(groupPosition);
                expandFlag = groupPosition;
            } else if (expandFlag == groupPosition) {
                explistview.collapseGroup(expandFlag);
                expandFlag = -1;
            } else {
                explistview.collapseGroup(expandFlag);
                // 展开被选的group
                explistview.expandGroup(groupPosition);
                // 设置被选中的group置于顶端
                explistview.setSelectedGroup(groupPosition);
                expandFlag = groupPosition;
            }
            return true;
        }
    }
}

