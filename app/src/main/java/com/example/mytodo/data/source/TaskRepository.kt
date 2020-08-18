package com.example.mytodo.data.source

import android.util.Log
import com.example.mytodo.data.model.Task


class TaskRepository(
    val tasksRemoteDataSource: TaskDataSource,
    private val tasksLocalDataSource: TaskDataSource
) : TaskDataSource {

    var cachedTasks: LinkedHashMap<String, Task> = LinkedHashMap()

    private var cacheIsDirty = false

    override fun getTasks(callback: TaskDataSource.LoadTasksCallback) {
        Log.d("Order ", "TaskRepository getTasks(callback: TaskDataSource.LoadTasksCallback)" +
                "cachedTasks is $cachedTasks & cacheIsDirty is $cacheIsDirty")

        if (cachedTasks.isNotEmpty() && !cacheIsDirty) {
            callback.onTasksLoaded(ArrayList(cachedTasks.values))
            return
        }

        if (cacheIsDirty) {
            // If the cache is dirty we need to fetch new data from the network
            getTasksFromRemoteDataSource(callback)
        } else {
            // Query the local storage if available. If not, query the network
            tasksLocalDataSource.getTasks(object : TaskDataSource.LoadTasksCallback {
                override fun onTasksLoaded(tasks: List<Task>) {

                    refreshCache(tasks)
                    callback.onTasksLoaded(ArrayList(cachedTasks.values))
                }

                override fun onDataNotAvailable() {
                    Log.d("Order ", "tasksLocalDataSource.getTasks -> onDataNotAvailable()")
                    getTasksFromRemoteDataSource(callback)
                }
            })
        }
    }

    override fun getTask(taskId: String, callback: TaskDataSource.GetTaskCallback) {

        val taskInCache = getTaskWithId(taskId)

        // Respond immediately with cache if available
        if (taskInCache != null) {
            callback.onTaskLoaded(taskInCache)
            return
        }

        // Load from server/persisted if needed

        // Is the task in the local data source? If not, query the network
        tasksLocalDataSource.getTask(taskId, object : TaskDataSource.GetTaskCallback {
            override fun onTaskLoaded(task: Task) {

                // Do in memory cache update to keep the app UI up to date
                cacheAndPerform(task) {
                    callback.onTaskLoaded(it)
                }
            }

            override fun onDataNotAvailable() {

                tasksRemoteDataSource.getTask(taskId, object : TaskDataSource.GetTaskCallback {
                    override fun onTaskLoaded(task: Task) {

                        // Do in memory cache update to keep the app UI up to date
                        cacheAndPerform(task) {
                            callback.onTaskLoaded(it)
                        }
                    }

                    override fun onDataNotAvailable() {
                        callback.onDataNotAvailable()
                    }
                })
            }

        })
    }

    override fun saveTask(task: Task) {

        cacheAndPerform(task) {
            tasksRemoteDataSource.saveTask(it)
            tasksLocalDataSource.saveTask(it)
        }
    }

    override fun completeTask(task: Task) {

        cacheAndPerform(task) {
            it.isCompleted = 1
            tasksRemoteDataSource.completeTask(it)
            tasksLocalDataSource.completeTask(it)
        }
    }

    override fun completeTask(taskId: String) {

        getTaskWithId(taskId)?.let {
            completeTask(it)
        }
    }

    override fun activateTask(task: Task) {

        // Do in memory cache update to keep the app UI up to date
        cacheAndPerform(task) {
            it.isCompleted = 0
            tasksRemoteDataSource.activateTask(it)
            tasksLocalDataSource.activateTask(it)
        }
    }

    override fun activateTask(taskId: String) {

        getTaskWithId(taskId)?.let {
            activateTask(it)
        }
    }

    override fun clearCompletedTasks() {

        tasksRemoteDataSource.clearCompletedTasks()
        tasksLocalDataSource.clearCompletedTasks()

        cachedTasks = cachedTasks.filterValues {
            it.isCompleted == 0
        } as LinkedHashMap<String, Task>
    }

    override fun refreshTasks() {

        // cacheIsDirty = true
    }

    override fun deleteAllTasks() {

        tasksRemoteDataSource.deleteAllTasks()
        tasksLocalDataSource.deleteAllTasks()
        cachedTasks.clear()
    }

    override fun deleteTask(taskId: String) {

        tasksRemoteDataSource.deleteTask(taskId)
        tasksLocalDataSource.deleteTask(taskId)
        cachedTasks.remove(taskId)
    }

    private fun getTasksFromRemoteDataSource(callback: TaskDataSource.LoadTasksCallback) {
        tasksRemoteDataSource.getTasks(object : TaskDataSource.LoadTasksCallback {
            override fun onTasksLoaded(tasks: List<Task>) {
                Log.d("Order ", "TaskRepos -> getTasksFromRemoteDataSource -> tasks.size = ${tasks.size}")
                refreshCache(tasks)
                refreshLocalDataSource(tasks)
                callback.onTasksLoaded(ArrayList(cachedTasks.values))
            }

            override fun onDataNotAvailable() {
                callback.onDataNotAvailable()
            }
        })
    }

    private fun refreshCache(tasks: List<Task>) {
        cachedTasks.clear()
        tasks.forEach {
            cacheAndPerform(it){}
        }
        cacheIsDirty = false
    }

    private inline fun cacheAndPerform(task: Task, perform: (Task) -> Unit) {
        val cachedTask = Task(task.title, task.description, task.id).apply {
            isCompleted = task.isCompleted
        }

        cachedTasks.put(cachedTask.id, cachedTask)
        perform(cachedTask)
    }

    private fun refreshLocalDataSource(tasks: List<Task>) {
        tasksLocalDataSource.deleteAllTasks()
        for (task in tasks) {
            tasksLocalDataSource.saveTask(task)
        }
    }

    private fun getTaskWithId(id: String) = cachedTasks[id]

    companion object {

        private var INSTANCE: TaskRepository? = null
        @JvmStatic
        fun getInstance(
            tasksRemoteDataSource: TaskDataSource,
            tasksLocalDataSource: TaskDataSource
        ): TaskRepository {
            return INSTANCE ?: TaskRepository(tasksRemoteDataSource, tasksLocalDataSource)
                .apply { INSTANCE = this }
        }

        @JvmStatic fun destroyInstance() {
            INSTANCE = null
        }
    }
}