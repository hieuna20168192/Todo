package com.example.mytodo

import android.content.Context
import com.example.mytodo.data.source.TasksRepository
import com.example.mytodo.data.source.local.TasksLocalDataSource
import com.example.mytodo.data.source.local.dao.TaskDaoImpl
import com.example.mytodo.data.source.remote.TaskRemoteDataSource

object Injection {
    fun provideTasksRepository(context: Context): TasksRepository {
        val localDataSource = TasksLocalDataSource.getInstance(TaskDaoImpl.getInstance(context))
        val remoteDataSource = TaskRemoteDataSource
        val repository =  TasksRepository.getInstance(localDataSource, remoteDataSource)
        return TasksRepository.getInstance(localDataSource,
        remoteDataSource)
    }
}