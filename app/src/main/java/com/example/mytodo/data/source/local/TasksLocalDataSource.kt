package com.example.mytodo.data.source.local

import android.util.Log
import com.example.mytodo.data.model.Task
import com.example.mytodo.data.source.TaskDataSource
import com.example.mytodo.data.source.local.dao.TaskDao
import com.example.mytodo.utils.AppExecutors

class TasksLocalDataSource private constructor(
    private val appExecutors: AppExecutors,
    private val taskDao: TaskDao
) : TaskDataSource {

    override fun getTasks(callback: TaskDataSource.LoadTasksCallback) {
        Log.d("Order ", "TasksLocalDataSource getTasks(callback: TaskDataSource.LoadTasksCallback)")
        appExecutors.diskIO.execute {
            val tasks = taskDao.getTasks()
            appExecutors.mainThread.execute {
                if (tasks.isEmpty()) {
                    // This will be called if the table is new or just empty
                    callback.onDataNotAvailable()
                } else {
                    callback.onTasksLoaded(tasks)
                }
            }
        }
    }

    override fun getTask(taskId: String, callback: TaskDataSource.GetTaskCallback) {
        Log.d("Order ", "TasksLocalDataSource getTask(taskId: String, callback: TaskDataSource.GetTaskCallback)")

        appExecutors.diskIO.execute {
            val task = taskDao.getTaskById(taskId)
            appExecutors.mainThread.execute {
                if (task != null) {
                    callback.onTaskLoaded(task)
                } else {
                    callback.onDataNotAvailable()
                }
            }
        }
    }

    override fun saveTask(task: Task) {
        Log.d("Order ", "TasksLocalDataSource saveTask(task: Task)")

        appExecutors.diskIO.execute {
            taskDao.insertTask(task)
        }
    }

    override fun completeTask(task: Task) {
        Log.d("Order ", "TasksLocalDataSource completeTask(task: Task)")

        appExecutors.diskIO.execute {
            taskDao.updateCompleted(task.id, true)
        }
    }

    override fun completeTask(taskId: String) {
        Log.d("Order ", "TasksLocalDataSource completeTask(taskId: String)")

        // Not required for the local data source
    }

    override fun activateTask(task: Task) {
        Log.d("Order ", "TasksLocalDataSource activateTask(task: Task)")

        appExecutors.diskIO.execute {
            taskDao.updateCompleted(task.id, false)
        }
    }

    override fun activateTask(taskId: String) {
        Log.d("Order ", "TasksLocalDataSource activateTask(taskId: String)")

        // Not required for the local data source
    }

    override fun clearCompletedTasks() {
        Log.d("Order ", "TasksLocalDataSource clearCompletedTasks()")

        appExecutors.diskIO.execute {
            taskDao.deleteCompletedTasks()
        }
    }

    override fun refreshTasks() {
        Log.d("Order ", "TasksLocalDataSource refreshTasks()")

        // Not required
    }

    override fun deleteAllTasks() {
        Log.d("Order ", "TasksLocalDataSource deleteAllTasks()")

        appExecutors.diskIO.execute {
            taskDao.deleteTasks()
        }
    }

    override fun deleteTask(taskId: String) {
        Log.d("Order ", "TasksLocalDataSource deleteTask(taskId: String)")
        appExecutors.diskIO.execute {
            taskDao.deleteTaskById(taskId)
        }
    }

    companion object {
        private var INSTANCE: TasksLocalDataSource? = null

        @JvmStatic
        fun getInstance(appExecutors: AppExecutors, tasksDao: TaskDao): TasksLocalDataSource {

            if (INSTANCE == null) {
                synchronized(TasksLocalDataSource::javaClass) {
                    INSTANCE = TasksLocalDataSource(appExecutors, tasksDao)
                }
            }
            return INSTANCE!!
        }
    }
}

