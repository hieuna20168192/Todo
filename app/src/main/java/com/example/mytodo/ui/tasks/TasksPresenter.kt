package com.example.mytodo.ui.tasks

import com.example.mytodo.data.model.Task
import com.example.mytodo.data.source.TasksRepository
import com.example.mytodo.data.source.local.base.OnDataLoadedCallback

class TasksPresenter (
    private val view: TasksContract.View,
    private val repository: TasksRepository
) : TasksContract.Presenter {

    override fun start() {
        getTasks()
    }

    override fun addTask(task: Task) {
        repository.addTask(task, object : OnDataLoadedCallback<Boolean> {
            override fun onSuccess(data: Boolean) {
                val msgId = if (data) "Success" else "Error"
                view.toast(msgId)
            }

            override fun onFailure(exception: Exception) {
                view.toast(exception)
            }
        })
    }

    override fun getTasks() {
        repository.getTasks(object : OnDataLoadedCallback<List<Task>>{
            override fun onSuccess(data: List<Task>) {
                view.showTasks(data)
            }

            override fun onFailure(exception: Exception) {
                view.toast(exception)
            }

        })
    }

}