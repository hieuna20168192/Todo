package com.example.mytodo.data.source.remote

import android.os.Handler
import com.example.mytodo.data.model.Task
import com.example.mytodo.data.source.TaskDataSource
import com.google.common.collect.Lists

object TaskRemoteDataSource : TaskDataSource {

    private const val SERVICE_LATENCY_IN_MILLIS = 5000L

    var TASKS_SERVICE_DATA = LinkedHashMap<String, Task>()

    init {
        addTask("Build tower in Pisa", "Ground looks good, no foundation work required.")
        addTask("Finish bridge in Tacoma", "Found awesome girders at half the cost!")
    }

    private fun addTask(title: String, description: String) {
        val newTask = Task(title, description)
        TASKS_SERVICE_DATA.put(newTask.id, newTask)
    }

    override fun getTasks(callback: TaskDataSource.LoadTasksCallback) {

        val tasks = Lists.newArrayList(TASKS_SERVICE_DATA.values)
        Handler().postDelayed({
            callback.onTasksLoaded(tasks)
        }, SERVICE_LATENCY_IN_MILLIS)
    }

    override fun getTask(taskId: String, callback: TaskDataSource.GetTaskCallback) {
        val task = TASKS_SERVICE_DATA[taskId]

        // Simulate network by delaying the execution
        with(Handler()) {
            if (task != null) {
                postDelayed({ callback.onTaskLoaded(task)}, SERVICE_LATENCY_IN_MILLIS)
            } else {
                postDelayed({ callback.onDataNotAvailable()}, SERVICE_LATENCY_IN_MILLIS)
            }
        }
    }

    override fun saveTask(task: Task) {
        TASKS_SERVICE_DATA.put(task.id, task)
    }

    override fun completeTask(task: Task) {
        val completedTask = Task(task.title, task.description, task.id).apply {
            isCompleted = 1
        }
        TASKS_SERVICE_DATA.put(task.id, completedTask)
    }

    override fun completeTask(taskId: String) {

    }

    override fun activateTask(task: Task) {
        val activeTask = Task(task.title, task.description, task.id)
        TASKS_SERVICE_DATA.put(task.id, activeTask)
    }

    override fun activateTask(taskId: String) {

    }

    override fun clearCompletedTasks() {
        TASKS_SERVICE_DATA = TASKS_SERVICE_DATA.filterValues {
            it.isCompleted == 0
        } as LinkedHashMap<String, Task>
    }

    override fun refreshTasks() {

    }

    override fun deleteAllTasks() {
        TASKS_SERVICE_DATA.clear()
    }

    override fun deleteTask(taskId: String) {

        TASKS_SERVICE_DATA.remove(taskId)
    }

}