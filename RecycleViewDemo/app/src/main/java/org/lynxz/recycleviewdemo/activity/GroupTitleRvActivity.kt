package org.lynxz.recycleviewdemo.activity

import android.support.v7.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_group_title_rv.*
import org.lynxz.recycleviewdemo.R
import org.lynxz.recycleviewdemo.adapter.GroupNameAdapter
import org.lynxz.recycleviewdemo.bean.GroupItem
import org.lynxz.recycleviewdemo.decoration.SimpleDecoration

/**
 * RecyclerView 分组粘性头
 * 参考: http://blog.csdn.net/binbinqq86/article/details/54427505
 * */
class GroupTitleRvActivity : BaseActivity() {

    override fun getLayoutRes() = R.layout.activity_group_title_rv

    override fun afterCreate() {
        val data = mutableListOf<GroupItem>().apply {
            var groupName = 'a'
            for (i in 1..100) {
                if (i % 5 == 0) {
                    groupName += 1
                }
                add(GroupItem(i.toString(), groupName.toString()))
            }
        }

        rv_group_title.apply {
            layoutManager = LinearLayoutManager(this@GroupTitleRvActivity)
            adapter = GroupNameAdapter(this@GroupTitleRvActivity, data).apply {
                addItemDecoration(object : SimpleDecoration() {
                    override fun getGroupName(childAdapterPosition: Int) = data[childAdapterPosition].groupName
                })
            }
        }
    }
}
