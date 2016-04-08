package org.lynxz.explandablelistviewdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.Toast;

import org.lynxz.explandablelistviewdemo.adapter.AnimatedAdapter;
import org.lynxz.explandablelistviewdemo.bean.ChildItem;
import org.lynxz.explandablelistviewdemo.bean.GroupItem;
import org.lynxz.explandablelistviewdemo.widget.AnimatedExpandableListView;

import java.util.ArrayList;
import java.util.List;

//public class AnimatedActivity extends AppCompatActivity {

/**
 * This is an example usage of the AnimatedExpandableListView class.
 * <p/>
 * It is an activity that holds a listview which is populated with 100 groups
 * where each group has from 1 to 100 children (so the first group will have one
 * child, the second will have two children and so on...).
 */
public class AnimatedActivity extends AppCompatActivity {
    private AnimatedExpandableListView listView;
    private AnimatedAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animated);

        List<GroupItem> items = new ArrayList<GroupItem>();

        // Populate our list with groups and it's children
        for (int i = 1; i < 20; i++) {
            GroupItem item = new GroupItem();

            item.title = "Group " + i;

            for (int j = 0; j < i; j++) {
                ChildItem child = new ChildItem();
                child.title = "Awesome item " + j;
                child.hint = "Too awesome";

                item.items.add(child);
            }

            items.add(item);
        }

        adapter = new AnimatedAdapter(this);
        adapter.setData(items);

        listView = (AnimatedExpandableListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);

        // In order to show animations, we need to use a custom click handler
        // for our ExpandableListView.
        listView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                // We call collapseGroupWithAnimation(int) and
                // expandGroupWithAnimation(int) to animate group
                // expansion/collapse.
                if (listView.isGroupExpanded(groupPosition)) {
                    listView.collapseGroupWithAnimation(groupPosition);
                } else {
                    listView.expandGroupWithAnimation(groupPosition);
                }
                return true;
            }
        });

        listView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Toast.makeText(AnimatedActivity.this, "child-" + groupPosition + "-" + childPosition + "-" + id, Toast.LENGTH_SHORT).show();
                return false;
            }
        });
    }
}
