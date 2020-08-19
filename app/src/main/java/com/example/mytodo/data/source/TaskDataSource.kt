package com.example.mytodo.data.source

import com.example.mytodo.data.model.Task

interface TaskDataSource {

    interface LoadTasksCallback{

        fun onTasksLoaded(tasks: List<Task>)

        fun onDataNotAvailable()
    }

    interface GetTaskCallback {

        fun onTaskLoaded(task: Task)

        fun onDataNotAvailable()
    }

    fun getTasks(callback: LoadTasksCallback) {}

    fun getTask(taskId: String, callback: GetTaskCallback) {}

    fun saveTask(task: Task) {}

    fun completeTask(task: Task) {}

    fun completeTask(taskId: String) {}

    fun activateTask(task: Task) {}

    fun activateTask(taskId: String) {}

    fun clearCompletedTasks() {}

    fun refreshTasks() {}

    fun deleteAllTasks() {}

    fun deleteTask(taskId: String) {}
}

