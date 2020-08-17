package com.example.mytodo.data.source.local.dao

import android.content.Context
import com.example.mytodo.data.model.Task
import com.example.mytodo.data.source.local.base.AppDatabase

class TaskDaoImpl private constructor(context: Context) : TaskDao {

    private val database = AppDatabase.getInstance(context).writableDatabase

    override fun getTasks(): List<Task> {
        val cursor = database.query(Task.TABLE_NAME, null, null, null, null, null, null).apply {
            moveToFirst()
        }
        return mutableListOf<Task>().apply {
            while (!cursor.isAfterLast) {
                add(Task(cursor))
                cursor.moveToNext()
            }
            cursor.close()
        }
    }

    override fun addTask(task: Task): Boolean =
        database.insert(Task.TABLE_NAME, null, task.getContentValues()) > 0

    override fun deleteTask(id: String): Boolean {
        TODO("Not yet implemented")
    }

    override fun updateTask(task: Task) {
        TODO("Not yet implemented")
    }

    companion object {
        private var instance: TaskDaoImpl? = null

        fun getInstance(context: Context) =
            instance ?: TaskDaoImpl(context).also {
                instance = it
            }
    }
}