package com.example.mytodo.data.source.local.dao

import com.example.mytodo.data.model.Task

interface TaskDao {

    fun getTasks(): List<Task>

    fun addTask(task: Task): Boolean

    fun deleteTask(id: String): Boolean

    fun updateTask(task: Task)

}