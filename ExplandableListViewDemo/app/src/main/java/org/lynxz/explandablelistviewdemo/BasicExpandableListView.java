package org.lynxz.explandablelistviewdemo;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.Toast;

import org.lynxz.explandablelistviewdemo.adapter.BasicExpandableListViewAdapter;
import org.lynxz.explandablelistviewdemo.bean.FriendBean;
import org.lynxz.explandablelistviewdemo.bean.FriendGroupBean;

import java.util.ArrayList;
import java.util.List;


public class BasicExpandableListView extends Activity {

    private ExpandableListView mEl;
    private List<FriendGroupBean> mGroupArr;
    private List<List<FriendBean>> mChildArr;
    private BasicExpandableListViewAdapter mAdapter;
    private static final String TAG = BasicExpandableListView.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.basic_friend_list);
        initView();
        initData();
        initEvent();
    }

    protected void initView() {
        mEl = (ExpandableListView) findViewById(R.id.el_friend);
        //不现实groupItem前方系统自带的箭头指示标识符
        mEl.setGroupIndicator(null);
    }

    /**
     * 初始化数据
     */
    protected void initData() {
        mGroupArr = new ArrayList<>();
        mGroupArr.add(new FriendGroupBean("老师", 12));
        mGroupArr.add(new FriendGroupBean("朋友", 32));
        mGroupArr.add(new FriendGroupBean("北师大", 10));
        mGroupArr.add(new FriendGroupBean("我的好友", 2));

        mChildArr = new ArrayList<>();
        for (int i = 0; i < mGroupArr.size(); i++) {
            List<FriendBean> childList = new ArrayList<>();
            for (int j = 0, max = mGroupArr.get(i).count; j < max; j++) {
                FriendBean child = new FriendBean(i + "&" + j, "name-" + i + "-" + j, null);
                childList.add(child);
            }
            mChildArr.add(childList);
        }
    }

    private int expandGroupIndex = -1;

    protected void initEvent() {
        // 设置适配器
        mAdapter = new BasicExpandableListViewAdapter(this, mEl, mGroupArr, mChildArr);
        mEl.setAdapter(mAdapter);

        //        mEl.setLayoutAnimation(new LayoutAnimationController());

        // 设置 groupItem点击事件 -- 最多只展开一组
        mEl.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
                                        @Override
                                        public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                                            if (expandGroupIndex == -1) {
                                                mEl.expandGroup(groupPosition);
                                                //设置被选中的group置于顶端
                                                //mEl.setSelectedGroup(groupPosition);
                                                expandGroupIndex = groupPosition;
                                            } else if (expandGroupIndex == groupPosition) {
                                                mEl.collapseGroup(expandGroupIndex);
                                                expandGroupIndex = -1;
                                            } else {
                                                mEl.collapseGroup(expandGroupIndex);
                                                //展开被选的group
                                                mEl.expandGroup(groupPosition);
                                                mEl.setSelectedGroup(groupPosition);
                                                expandGroupIndex = groupPosition;
                                            }
                                            mAdapter.setExpandGroupIndex(expandGroupIndex);

                                            long packedPositionForGroup = mEl.getPackedPositionForGroup(groupPosition);
//                                            int packedPositionGroup = mEl.getPackedPositionGroup(packedPositionForGroup);
                                            int flatListPosition = mEl.getFlatListPosition(packedPositionForGroup);
                                            Log.d(TAG, "Group- " + flatListPosition + " - " + packedPositionForGroup);
                                            return true;
                                        }
                                    }
        );

        // 设置 group内部childItem点击事件
        mEl.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                                        @Override
                                        public boolean onChildClick(ExpandableListView parent, View v, int groupPosition,
                                                                    int childPosition, long id) {

                                            long packedPositionForChild = mEl.getPackedPositionForChild(groupPosition, childPosition);
//                                            int packedPositionChild = mEl.getPackedPositionChild(packedPositionForChild);
                                            int flatListPosition = mEl.getFlatListPosition(packedPositionForChild);
                                            Log.d(TAG, "Child- " + flatListPosition + " - " + packedPositionForChild);

                                            Toast.makeText(BasicExpandableListView.this, "group=" + groupPosition + "-child=" + childPosition, Toast.LENGTH_SHORT).show();
                                            return false;
                                        }
                                    }
        );
    }
}
