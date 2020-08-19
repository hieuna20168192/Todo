package com.example.mytodo.data.source.remote

import com.example.mytodo.data.source.TaskDataSource
import com.example.mytodo.data.source.remote.dao.RequestDao
import com.example.mytodo.utils.AppExecutors

class TaskRemoteDataSource(
    private val appExecutors: AppExecutors,
    private val requestDao: RequestDao
) : TaskDataSource {

    override fun getTasks(callback: TaskDataSource.LoadTasksCallback) {

        appExecutors.diskIO.execute {
            val tasks = requestDao.getTasks()
            appExecutors.mainThread.execute {
                if (tasks.isEmpty()) {
                    callback.onDataNotAvailable()
                } else {
                    callback.onTasksLoaded(tasks)
                }
            }
        }
    }

    override fun getTask(taskId: String, callback: TaskDataSource.GetTaskCallback) {
        appExecutors.diskIO.execute {
            val task = requestDao.getTaskById(taskId)
            appExecutors.mainThread.execute {
                if (task != null) {
                    callback.onTaskLoaded(task)
                } else {
                    callback.onDataNotAvailable()
                }
            }
        }
    }
}

