package com.example.mytodo.ui.tasks

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import com.example.mytodo.R
import com.example.mytodo.data.model.Task
import com.example.mytodo.ui.base.BaseRecyclerAdapter
import com.example.mytodo.ui.base.BaseViewHolder

class TaskAdapter(private val itemListener: TasksFragment.TaskItemListener) :
    BaseRecyclerAdapter<Task, TaskAdapter.TaskViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        Log.d("Order ", "onCreateViewHolder = $itemListener")
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.task_item, parent, false)
        return TaskViewHolder(itemView, itemListener)
    }

    class TaskViewHolder(itemView: View, itemListener: TasksFragment.TaskItemListener) :
        BaseViewHolder<Task>(itemView, itemListener) {
        override fun onBindData(task: Task) {
            super.onBindData(task)
            Log.d("Order ", "onBindData(task: Task)")
            with(itemView.findViewById<TextView>(R.id.title)) {
                text = task.titleForList
            }
            with(itemView.findViewById<CheckBox>(R.id.complete)) {
                // Active/completed task UI
                isChecked = (task.isCompleted == 1)
                val rowViewBackground =
                    if (task.isCompleted == 1) R.drawable.list_completed_touch_feedback
                    else R.drawable.touch_feedback
                itemView.setBackgroundResource(rowViewBackground)
                setOnClickListener {
                    if (task.isCompleted == 0) {
                        itemListener.onCompleteTaskClick(task)
                    } else {
                        itemListener.onActivateTaskClick(task)
                    }
                }
            }
        }

        override fun onHandleItemClick(task: Task) {
            itemListener.onTaskClick(task)
        }
    }
}