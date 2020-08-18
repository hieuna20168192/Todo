package com.example.mytodo.ui.tasks

import com.example.mytodo.data.model.Task
import com.example.mytodo.ui.base.BasePresenter
import com.example.mytodo.ui.base.BaseView

interface TasksContract {

    interface View: BaseView<Presenter> {

        var isActive: Boolean

        fun setLoadingIndicator(active: Boolean)

        fun showTasks(tasks: List<Task>)

        fun showAddTask()

        fun showTaskDetailsUi(taskId: String)

        fun showTaskMarkedComplete()

        fun showTaskMarkedActive()

        fun showCompletedTasksCleared()

        fun showLoadingTasksError()

        fun showNoTasks()

        fun showActiveFilterLabel()

        fun showCompletedFilterLabel()

        fun showAllFilterLabel()

        fun showNoActiveTasks()

        fun showNoCompletedTasks()

        fun showSuccessfullySavedMessage()

        fun showFilteringPopUpMenu()

    }

    interface Presenter : BasePresenter {

        var currentFiltering: TasksFilterType

        fun result(requestCode: Int, resultCode: Int)

        fun loadTasks(forceUpdate: Boolean)

        fun addNewTask()

        fun openTaskDetails(requestedTask: Task)

        fun completeTask(completedTask: Task)

        fun activateTask(activeTask: Task)

        fun clearCompletedTasks()
    }
}