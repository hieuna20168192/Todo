package com.example.mytodo.ui.tasks

import com.example.mytodo.data.model.Task
import com.example.mytodo.ui.base.BasePresenter
import com.example.mytodo.ui.base.BaseView

interface TasksContract {

    interface View: BaseView<Presenter> {
        fun showTasks(tasks: List<Task>)
    }

    interface Presenter : BasePresenter {

        var currentFiltering: TasksFilterType

        fun addTask(task: Task)

        fun getTasks()
    }
}