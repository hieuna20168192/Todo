package com.example.mytodo.data.source.remote

import com.example.mytodo.data.model.Task
import com.example.mytodo.data.source.TaskDataSource

object TaskRemoteDataSource : TaskDataSource {

    override fun getTasks(callback: TaskDataSource.LoadTasksCallback) {
        TODO("Not yet implemented")
    }

    override fun getTask(taskId: String, callback: TaskDataSource.GetTaskCallback) {
        TODO("Not yet implemented")
    }

    override fun saveTask(task: Task) {
        TODO("Not yet implemented")
    }

    override fun completeTask(task: Task) {
        TODO("Not yet implemented")
    }

    override fun completeTask(taskId: String) {
        TODO("Not yet implemented")
    }

    override fun activateTask(task: Task) {
        TODO("Not yet implemented")
    }

    override fun activateTask(taskId: String) {
        TODO("Not yet implemented")
    }

    override fun clearCompletedTasks() {
        TODO("Not yet implemented")
    }

    override fun refreshTasks() {
        TODO("Not yet implemented")
    }

    override fun deleteAllTasks() {
        TODO("Not yet implemented")
    }

    override fun deleteTask(taskId: String) {
        TODO("Not yet implemented")
    }
    
}