package org.lynxz.explandablelistviewdemo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import org.lynxz.explandablelistviewdemo.R;

import java.util.List;

/**
 * Created by zxz on 2016/5/26.
 */
public class ElChildAdapter extends BaseExpandableListAdapter {

    private final List<List<String>> mData;
    private final LayoutInflater mInflater;

    public ElChildAdapter(Context cxt, List<List<String>> data) {
        mInflater = LayoutInflater.from(cxt);
        mData = data;
    }

    @Override
    public int getGroupCount() {
        return mData.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mData.get(groupPosition).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return null;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return null;
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
        CommonHolder holder = null;
        if (convertView == null) {
            holder = new CommonHolder();
            convertView = mInflater.inflate(R.layout.child, null);
            holder.tvCommon = (TextView) convertView.findViewById(R.id.childto);
            convertView.setTag(holder);
        } else {
            holder = (CommonHolder) convertView.getTag();
        }
        holder.tvCommon.setText("childGroup " + groupPosition);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        CommonHolder holder = null;
        if (convertView == null) {
            holder = new CommonHolder();
            convertView = mInflater.inflate(R.layout.child, null);
            holder.tvCommon = (TextView) convertView.findViewById(R.id.childto);
            convertView.setTag(holder);
        } else {
            holder = (CommonHolder) convertView.getTag();
        }
        holder.tvCommon.setText(" child --  " + childPosition);
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    class CommonHolder {
        TextView tvCommon;
    }
}
