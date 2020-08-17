package com.example.mytodo.ui.tasks

import com.example.mytodo.data.model.Task
import com.example.mytodo.ui.base.BasePresenter
import com.example.mytodo.ui.base.BaseView

interface TasksContract {

    interface View: BaseView {
        fun showTasks(tasks: List<Task>)
    }

    interface Presenter : BasePresenter {
        fun addTask(task: Task)

        fun getTasks()
    }
}