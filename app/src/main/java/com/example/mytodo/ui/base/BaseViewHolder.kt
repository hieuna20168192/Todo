package com.example.mytodo.ui.base

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.mytodo.ui.tasks.TasksFragment

open class BaseViewHolder<T>(itemView: View, itemListener: TasksFragment.TaskItemListener) : RecyclerView.ViewHolder(itemView) {

    protected var itemData: T? = null
    protected lateinit var itemListener: TasksFragment.TaskItemListener

    init {
        itemView.setOnClickListener {
            itemData?.let(::onHandleItemClick)
        }
        this.itemListener = itemListener
    }

    open fun onBindData(itemData: T) {
        this.itemData = itemData
    }

    open fun onHandleItemClick(mainItem: T) {

    }
}
