package com.example.mytodo.data.source.remote.dao

import com.example.mytodo.data.model.Task

interface RequestDao {

    fun getTasks(): List<Task>

    fun getTaskById(taskId: String): Task?

}

