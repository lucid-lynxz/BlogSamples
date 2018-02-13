package org.lynxz.recycleviewdemo.decoration

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.view.View

/**
 * Created by lynxz on 05/02/2018.
 * 博客: https://juejin.im/user/5812c2b0570c3500605a15ff
 */
abstract class SimpleDecoration : RecyclerView.ItemDecoration() {
    // 指定 group name 行高
    private var groupNameHeight = 138

    private val mGroupNameRectPainter by lazy {
        Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.LTGRAY
        }
    }

    private val mGroupNamePainter by lazy {
        Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.BLACK
            textSize = 40f
        }
    }

    /**
     * 获取 item 对应的 group name
     * */
    abstract fun getGroupName(childAdapterPosition: Int): String?

    override fun getItemOffsets(outRect: Rect?, view: View?, parent: RecyclerView?, state: RecyclerView.State?) {
        super.getItemOffsets(outRect, view, parent, state)
        parent?.let {
            val childAdapterPosition = it.getChildAdapterPosition(view)
            val currentGroupName = getGroupName(childAdapterPosition)

            if (currentGroupName.isNullOrEmpty()) {
                return
            }

            // 首条记录添加 group name
            // 若 当前item与前一条item的 group name不一致, 表明是新的group,需要添加 group name
            if (childAdapterPosition == 0
                    || currentGroupName != getGroupName(childAdapterPosition - 1)) {
                outRect?.top = groupNameHeight
            }
        }
    }

    override fun onDrawOver(canvas: Canvas?, parent: RecyclerView?, state: RecyclerView.State?) {
        super.onDrawOver(canvas, parent, state)
        // 绘制当前group name
        parent?.let {
            val left = it.left + it.paddingLeft
            val right = it.right + it.paddingRight
            // 悬浮 group name 行需要移动的距离
            var offsetY = 0f

            // 悬浮 group name 内容
            var topGroupName: String? = null

            // 屏幕上可见的item数量
            for (i in 0 until it.childCount) {
                val item = it.getChildAt(i)
                val pos = it.getChildAdapterPosition(item)
                val currentGroupName = getGroupName(pos)

                if (currentGroupName.isNullOrEmpty()) continue

                // 当前屏幕第一个可见 item 对应的 group name
                if (i == 0) {
                    topGroupName = currentGroupName
                }

                val viewTop = item.top + it.paddingTop

                if (pos == 0 || currentGroupName != getGroupName(pos - 1)) {
                    canvas?.drawRect(left.toFloat(), viewTop.toFloat() - groupNameHeight, right.toFloat(), viewTop.toFloat(), mGroupNameRectPainter)
                    canvas?.drawText(currentGroupName, left.toFloat(), (viewTop - groupNameHeight / 2).toFloat(), mGroupNamePainter)

                    if (viewTop in groupNameHeight..2 * groupNameHeight) {
                        offsetY = (viewTop - 2 * groupNameHeight).toFloat()
                    }
                }

                // 绘制悬浮 group name 行
                if (topGroupName.isNullOrEmpty()) {
                    return
                }

                canvas?.save()
                canvas?.translate(0f, offsetY)
                canvas?.drawRect(left.toFloat(), 0f, right.toFloat(), groupNameHeight.toFloat(), mGroupNameRectPainter)
                canvas?.drawText(topGroupName, left.toFloat(), groupNameHeight / 2.toFloat(), mGroupNamePainter)
                canvas?.restore()
            }
        }
    }
}