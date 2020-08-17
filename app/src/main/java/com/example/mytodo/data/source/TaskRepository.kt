package com.example.mytodo.data.source

import com.example.mytodo.data.model.Task
import com.example.mytodo.data.source.local.base.OnDataLoadedCallback

class TasksRepository private constructor(
    private val local: TaskDataSource.Local,
    private val remote: TaskDataSource.Remote
) : TaskDataSource.Local, TaskDataSource.Remote {

    override fun getTasks(callback: OnDataLoadedCallback<List<Task>>) {
        local.getTasks(callback)
    }

    override fun addTask(task: Task, callback: OnDataLoadedCallback<Boolean>) {
        local.addTask(task, callback)
    }

    override fun deleteTask(id: String, callback: OnDataLoadedCallback<Boolean>) {

    }

    override fun updateTask(task: Task, callback: OnDataLoadedCallback<Task>) {

    }

    companion object {
        private var instance: TasksRepository? = null
        fun getInstance(local: TaskDataSource.Local, remote: TaskDataSource.Remote) =
            instance ?: TasksRepository(local, remote).also {
                instance = it
            }
    }
}