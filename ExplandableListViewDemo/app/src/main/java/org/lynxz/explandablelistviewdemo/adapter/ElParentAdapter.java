package org.lynxz.explandablelistviewdemo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import org.lynxz.explandablelistviewdemo.R;
import org.lynxz.explandablelistviewdemo.widget.SecondLevelExpandableListView;

import java.util.List;

/**
 * Created by zxz on 2016/5/26.
 */
public class ElParentAdapter extends BaseExpandableListAdapter {

    private final Context mCxt;
    private final List<String> mParentGroup;
    private final List<List<String>> mChildGroupList;
    private final LayoutInflater mInflater;

    public ElParentAdapter(Context cxt, List<String> parentGroupList, List<List<String>> childGroupList) {
        mCxt = cxt;
        mInflater = LayoutInflater.from(cxt);
        mParentGroup = parentGroupList;
        mChildGroupList = childGroupList;
    }

    @Override
    public int getGroupCount() {
        return mParentGroup.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mChildGroupList.get(groupPosition).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mParentGroup.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mChildGroupList.get(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupHolder holder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.child, null);
            holder = new GroupHolder();
            holder.tvGroup = (TextView) convertView.findViewById(R.id.childto);
            convertView.setTag(holder);
        } else {
            holder = (GroupHolder) convertView.getTag();
        }
        holder.tvGroup.setText(mParentGroup.get(groupPosition));
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildHolder holder = null;
        if (convertView == null) {
            holder = new ChildHolder();
            convertView = mInflater.inflate(R.layout.item_3_level, null);
            holder.exChild = (SecondLevelExpandableListView) convertView.findViewById(R.id.el_child);
            convertView.setTag(holder);
        } else {
            holder = (ChildHolder) convertView.getTag();
        }

        ElChildAdapter elChildAdapter = new ElChildAdapter(mCxt, mChildGroupList);
        holder.exChild.setAdapter(elChildAdapter);
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    class GroupHolder {
        TextView tvGroup;
    }

    class ChildHolder {
        SecondLevelExpandableListView exChild;
    }
}
