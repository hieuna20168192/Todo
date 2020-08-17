package com.example.mytodo.data.source.local

import com.example.mytodo.data.model.Task
import com.example.mytodo.data.source.TaskDataSource
import com.example.mytodo.data.source.local.base.EmptyInput
import com.example.mytodo.data.source.local.base.LocalAsyncTask
import com.example.mytodo.data.source.local.base.OnDataLoadedCallback
import com.example.mytodo.data.source.local.dao.TaskDao

class TaskLocalDataSource private constructor(
    private val taskDao: TaskDao
) : TaskDataSource.Local {
    override fun getTasks(callback: OnDataLoadedCallback<List<Task>>) {
        LocalAsyncTask<EmptyInput, List<Task>>(callback) {
            taskDao.getTasks()
        }.execute(EmptyInput)
    }

    override fun addTask(task: Task, callback: OnDataLoadedCallback<Boolean>) {
        LocalAsyncTask<Task, Boolean>(callback) {
            taskDao.addTask(task)
        }.execute(task)
    }

    override fun deleteTask(id: String, callback: OnDataLoadedCallback<Boolean>) {
        TODO("Not yet implemented")
    }

    override fun updateTask(task: Task, callback: OnDataLoadedCallback<Task>) {
        TODO("Not yet implemented")
    }

    companion object {
        private var instance: TaskLocalDataSource? = null
        fun getInstance(taskDao: TaskDao): TaskLocalDataSource =
            instance ?: getInstance(taskDao).also {
                instance = it
            }
    }
}