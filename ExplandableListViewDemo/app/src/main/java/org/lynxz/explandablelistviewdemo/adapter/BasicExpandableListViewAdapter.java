package org.lynxz.explandablelistviewdemo.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import org.lynxz.explandablelistviewdemo.R;
import org.lynxz.explandablelistviewdemo.bean.FriendBean;
import org.lynxz.explandablelistviewdemo.bean.FriendGroupBean;

import java.util.List;

public class BasicExpandableListViewAdapter extends BaseExpandableListAdapter implements AbsListView.OnScrollListener {
    private ExpandableListView el;
    private LayoutInflater layoutInflater;
    private List<FriendGroupBean> groupList;
    private List<List<FriendBean>> childList;
    private boolean isScrolling = false;
    private int expandGroupIndex = -1;//当前展开的group序号,也是用于延迟加载图片


    public BasicExpandableListViewAdapter(@NonNull Context cxt, ExpandableListView el, @NonNull List<FriendGroupBean> groupList, @NonNull List<List<FriendBean>> childList) {
        this.layoutInflater = LayoutInflater.from(cxt);
        this.el = el;
        this.groupList = groupList;
        this.childList = childList;

        //监听listview滚动状态,用于延迟加载图片
        this.el.setOnScrollListener(this);
    }

    public void setExpandGroupIndex(int expandGroupIndex) {
        this.expandGroupIndex = expandGroupIndex;
    }

    @Override
    public int getGroupCount() {
        return this.groupList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.childList.get(groupPosition).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.groupList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this.childList.get(groupPosition).get(childPosition);
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


    /**
     * getGroupView/getChildView 跟 ListView的getView 蛮像的,可以使用复用机制
     */
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupHolder groupHolder = null;
        if (convertView == null) {
            groupHolder = new GroupHolder();
            convertView = this.layoutInflater.inflate(R.layout.item_fl_group, null);
            groupHolder.iv = (ImageView) convertView.findViewById(R.id.iv_group);
            groupHolder.tvName = (TextView) convertView.findViewById(R.id.tv_group_name);
            convertView.setTag(groupHolder);
        } else {
            groupHolder = (GroupHolder) convertView.getTag();
        }

        groupHolder.tvName.setText(this.groupList.get(groupPosition).toString());
        if (isExpanded) {
            groupHolder.iv.setImageResource(R.drawable.iconfont_triangle_expand);
        } else {
            groupHolder.iv.setImageResource(R.drawable.iconfont_triangle_normal);
        }
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildHolder childHolder = null;
        if (convertView == null) {
            childHolder = new ChildHolder();
            convertView = this.layoutInflater.inflate(R.layout.item_fl_child, null);
            childHolder.photo = (ImageView) convertView.findViewById(R.id.iv_friend);
            childHolder.tvName = (TextView) convertView.findViewById(R.id.tv_name);
            convertView.setTag(childHolder);
        } else {
            childHolder = (ChildHolder) convertView.getTag();
        }

        FriendBean child = this.childList.get(groupPosition).get(childPosition);
        childHolder.tvName.setText(child.name);

        /*
        * 延时加载图片
        * 这里计算得到的firstVisiblePosition等位置是包含groupItem以及childItem在内的整个列表组合顺序
        * */
        if (this.expandGroupIndex >= 0 && !isScrolling) {
            if (!TextUtils.isEmpty(child.photoUrl)
                    && childPosition >= this.el.getFirstVisiblePosition()
                    && childPosition <= this.el.getLastVisiblePosition()) {
                //todo: imageLoader加载图片
            }
        }

        return convertView;
    }

    /**
     * 默认为false,表示childItem不可点击
     */
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

        if (scrollState == SCROLL_STATE_IDLE) {
            isScrolling = false;
            notifyDataSetChanged();
        } else {
            isScrolling = false;
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }

    class GroupHolder {
        ImageView iv;
        TextView tvName;
    }

    class ChildHolder {
        ImageView photo;
        TextView tvName;
    }
}
