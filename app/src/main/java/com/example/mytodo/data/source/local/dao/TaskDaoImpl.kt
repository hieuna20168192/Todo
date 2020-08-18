package com.example.mytodo.data.source.local.dao

import android.content.Context
import com.example.mytodo.data.model.Task
import com.example.mytodo.data.source.local.base.AppDatabase

class TaskDaoImpl private constructor(context: Context) : TaskDao {

    private val database = AppDatabase.getInstance(context).writableDatabase

    companion object {
        private var instance: TaskDaoImpl? = null

        fun getInstance(context: Context) =
            instance ?: TaskDaoImpl(context).also {
                instance = it
            }
    }

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

    override fun getTaskById(taskId: String): Task? {
        return null
    }

    override fun insertTask(task: Task) {
        database.insert(Task.TABLE_NAME, null, task.getContentValues())
    }

    override fun updateTask(task: Task): Int {
        return 0
    }

    override fun updateCompleted(taskId: String, completed: Boolean) {

    }

    override fun deleteTaskById(taskId: String): Int {
        return 0
    }

    override fun deleteTasks() {

    }

    override fun deleteCompletedTasks(): Int {
        return 0
    }
}