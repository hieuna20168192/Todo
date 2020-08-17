package com.example.mytodo.data.source

import com.example.mytodo.data.model.Task
import com.example.mytodo.data.source.local.base.OnDataLoadedCallback

interface TaskDataSource {

    interface Local {

        fun getTasks(callback: OnDataLoadedCallback<List<Task>>)

        fun addTask(task: Task, callback: OnDataLoadedCallback<Boolean>)

        fun deleteTask(id: String, callback: OnDataLoadedCallback<Boolean>)

        fun updateTask(task: Task, callback: OnDataLoadedCallback<Task>)
    }

    interface Remote
}