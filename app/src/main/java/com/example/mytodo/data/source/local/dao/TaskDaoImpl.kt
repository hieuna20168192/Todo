package com.example.mytodo.data.source.local.dao

import android.content.Context
import android.util.Log
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

        var selectionClause: String? = null
        val selectionArgs = taskId?.takeIf {
            it.isNotEmpty()
        }?.let {
            selectionClause = "${Task.ID} = ?"
            arrayOf(it)
        } ?: return Task()

        val cursor =
            database.query(Task.TABLE_NAME, null, selectionClause, selectionArgs, null, null, null)
                .apply {
                    moveToFirst()
                }

        return if (cursor != null) Task(cursor)
        else Task()

    }

    override fun insertTask(task: Task) {
        Log.d("Order ", "TaskDaoImpl insertTask()")
        database.insert(Task.TABLE_NAME, null, task.getContentValues())
    }

    override fun updateTask(task: Task): Int {
        val cv = task.getContentValues()
        var selectionClause: String? = null
        val selectionArgs = task.id?.takeIf {
            it.isNotEmpty()
        }?.let {
            selectionClause = "${Task.ID} = ?"
            arrayOf(it)
        } ?: return -1

        return database.update(Task.TABLE_NAME, cv, selectionClause, selectionArgs)

    }

    override fun updateCompleted(taskId: String, completed: Boolean) {
        Log.d("Order ", "TaskDaoImpl updateTask task.id = $taskId")
        var isCompleted = if (completed) 1 else 0

        if (taskId == null) return

        val queryString = "UPDATE ${Task.TABLE_NAME} SET ${Task.IS_COMPLETED}= ? " +
        "WHERE ${Task.ID}=?"
        database.execSQL(queryString, arrayOf(isCompleted, taskId))
    }

    override fun deleteTaskById(taskId: String): Int {

        var selectionClause: String? = null
        val selectionArgs = taskId?.takeIf {
            it.isNotEmpty()
        }?.let {
            selectionClause = "${Task.ID} = ?"
            arrayOf(it)
        } ?: return -1

        return database.delete(
            Task.TABLE_NAME, selectionClause, selectionArgs
        )
    }

    override fun deleteTasks() {
        database.delete(
            Task.TABLE_NAME, null, null
        )
        Log.d("Order ", "TaskDaoImpl deleteTasks()")
    }

    override fun deleteCompletedTasks(): Int {
        var selectionClause = "${Task.IS_COMPLETED} = ?"
        var selectionArgs = arrayOf("1")

        return database.delete(
            Task.TABLE_NAME, selectionClause, selectionArgs
        )
    }
}

