package com.example.mytodo.data.source.local.dao

import com.example.mytodo.data.model.Task

interface TaskDao {

    fun getTasks(): List<Task>

    fun getTaskById(taskId: String): Task?

    fun insertTask(task: Task)

    fun updateTask(task: Task): Int

    fun updateCompleted(taskId: String, completed: Boolean)

    fun deleteTaskById(taskId: String): Int

    fun deleteTasks()

    fun deleteCompletedTasks(): Int
}