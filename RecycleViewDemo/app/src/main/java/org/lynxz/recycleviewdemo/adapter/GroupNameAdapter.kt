package org.lynxz.recycleviewdemo.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import org.lynxz.recycleviewdemo.R
import org.lynxz.recycleviewdemo.adapter.GroupNameAdapter.GpViewHolder
import org.lynxz.recycleviewdemo.bean.GroupItem

/**
 * Created by lynxz on 05/02/2018.
 * 博客: https://juejin.im/user/5812c2b0570c3500605a15ff
 */
class GroupNameAdapter(private var cxt: Context,
                       private var dataList: List<GroupItem> = mutableListOf()) : RecyclerView.Adapter<GpViewHolder>() {
    private val mLayoutInflater by lazy { LayoutInflater.from(cxt) }

    override fun onBindViewHolder(holder: GpViewHolder?, position: Int) {
        holder?.tvName?.text = dataList[position].content
    }

    override fun getItemCount() = dataList.size

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int) = GpViewHolder(mLayoutInflater.inflate(R.layout.item_group_name, parent, false))

    inner class GpViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: TextView by lazy {
            itemView.findViewById(R.id.tv_group_name) as TextView
        }
    }
}