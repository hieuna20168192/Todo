package com.example.mytodo.ui.tasks

import android.util.Log
import com.example.mytodo.data.model.Task
import com.example.mytodo.data.source.TaskDataSource
import com.example.mytodo.data.source.TaskRepository

class TasksPresenter (
    private val taskRepository: TaskRepository,
    val tasksView: TasksContract.View
) : TasksContract.Presenter {
    override var currentFiltering = TasksFilterType.ALL_TASKS

    private var firstLoad = true

    init {
        tasksView.presenter = this
    }

    override fun result(requestCode: Int, resultCode: Int) {
        // If a task was successfully added, show snackbar
    }

    override fun loadTasks(forceUpdate: Boolean) {
        loadTasks(forceUpdate || firstLoad, true)
        firstLoad = false
    }

    override fun addNewTask() {
        tasksView.showAddTask()
    }

    override fun openTaskDetails(requestedTask: Task) {
        tasksView.showTaskDetailsUi(requestedTask.id)
    }

    override fun completeTask(completedTask: Task) {
        taskRepository.completeTask(completedTask)
        tasksView.showTaskMarkedComplete()
        loadTasks(false, false)
    }

    override fun activateTask(activeTask: Task) {

        taskRepository.activateTask(activeTask)
        tasksView.showTaskMarkedActive()
        loadTasks(false, false)
    }

    override fun clearCompletedTasks() {
        taskRepository.clearCompletedTasks()
        tasksView.showCompletedTasksCleared()
        loadTasks(false, false)
    }

    override fun start() {
        loadTasks(false)
    }

    private fun loadTasks(forceUpdate: Boolean, showLoadingUI: Boolean) {
        if (showLoadingUI) {
            tasksView.setLoadingIndicator(true)
        }
        if (forceUpdate) {
            taskRepository.refreshTasks()
        }

        taskRepository.getTasks(object : TaskDataSource.LoadTasksCallback{
            override fun onTasksLoaded(tasks: List<Task>) {
                val tasksToShow = ArrayList<Task>()
                Log.d("Order ", "TasksPresenter -> taskRepos -> onTasksLoaded -> tasks ${tasks.size}")
                for (task in tasks) {
                    when(currentFiltering) {
                        TasksFilterType.ALL_TASKS -> tasksToShow.add(task)
                        TasksFilterType.ACTIVE_TASKS -> if (task.isActive) {
                            tasksToShow.add(task)
                        }
                        TasksFilterType.COMPLETED_TASKS -> if (task.isCompleted == 1) {
                            tasksToShow.add(task)
                        }
                    }
                }
                Log.d("Order ", "TasksPresenter showLoadingUI is $showLoadingUI")
                // The view may not be able to handle UI updates anymore
                if (!tasksView.isActive) {
                    return
                }
                if (showLoadingUI) {
                    tasksView.setLoadingIndicator(false)
                }
                handTasks(tasksToShow)
            }

            override fun onDataNotAvailable() {
                if (!tasksView.isActive) {
                    return
                }
                tasksView.showLoadingTasksError()
            }
        })
    }

    private fun handTasks(tasks: ArrayList<Task>) {
        if (tasks.isEmpty()) {
            // Show a message indicating there are no tasks for that filter type
            handEmptyTasks()
        } else {
            // Show the list of tasks
            tasksView.showTasks(tasks)
            // Set the filter label's text
            showFilterLabel()
        }
    }

    private fun handEmptyTasks() {
        when (currentFiltering) {
            TasksFilterType.ACTIVE_TASKS -> tasksView.showNoActiveTasks()
            TasksFilterType.COMPLETED_TASKS -> tasksView.showNoCompletedTasks()
            else -> tasksView.showNoTasks()
        }
    }

    private fun showFilterLabel() {
        when (currentFiltering) {
            TasksFilterType.ACTIVE_TASKS -> tasksView.showActiveFilterLabel()
            TasksFilterType.COMPLETED_TASKS -> tasksView.showCompletedFilterLabel()
            else -> tasksView.showAllFilterLabel()
        }
    }
}