package com.example.mytodo

import android.content.Context
import com.example.mytodo.data.source.TaskRepository
import com.example.mytodo.data.source.local.TasksLocalDataSource
import com.example.mytodo.data.source.local.dao.TaskDaoImpl
import com.example.mytodo.data.source.remote.TaskRemoteDataSource
import com.example.mytodo.data.source.remote.dao.RequestHandler
import com.example.mytodo.utils.AppExecutors

object Injection {
    fun provideTasksRepository(context: Context): TaskRepository {

        return TaskRepository.getInstance(TaskRemoteDataSource(AppExecutors(), RequestHandler.getInstance()),
            TasksLocalDataSource.getInstance(AppExecutors(), TaskDaoImpl.getInstance(context)))
    }
}

