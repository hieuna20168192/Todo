package com.example.mytodo.ui.tasks

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mytodo.R
import com.example.mytodo.data.model.Task
import com.example.mytodo.ui.base.BaseRecyclerAdapter
import com.example.mytodo.ui.base.BaseViewHolder
import kotlinx.android.synthetic.main.task_item.view.*

class TaskAdapter : BaseRecyclerAdapter<Task, TaskAdapter.TaskViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.task_item, parent, false)
        return TaskViewHolder(itemView)
    }

    class TaskViewHolder(itemView: View) : BaseViewHolder<Task>(itemView) {

        override fun onBindData(itemData: Task) {
            super.onBindData(itemData)

            with(itemView) {
                title.text = itemData.title
            }
        }
    }
}