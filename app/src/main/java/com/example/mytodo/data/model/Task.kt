package com.example.mytodo.data.model

import android.content.ContentValues
import android.database.Cursor
import java.util.*

data class Task(
    val id: String = UUID.randomUUID().toString(),
    val title: String = "",
    val description: String = "",
    var isCompleted: Int = 0
) {

    constructor(cursor: Cursor) : this(
        id = cursor.getString(cursor.getColumnIndex(ID)),
        title = cursor.getString(cursor.getColumnIndex(TITLE)),
        description = cursor.getString(cursor.getColumnIndex(DESCRIPTION)),
        isCompleted = cursor.getInt(cursor.getColumnIndex(IS_COMPLETED))
    )

    fun getContentValues() = ContentValues().apply {
        put(ID, id)
        put(TITLE, title)
        put(DESCRIPTION, description)
        put(IS_COMPLETED, isCompleted)
    }

    val titleForList: String
        get() = if (title.isNotEmpty()) title else description
    val isActive: Boolean
        get() = isCompleted == 0

    val isEmpty: Boolean
        get() = title.isEmpty() && description.isEmpty()

    companion object {
        const val TABLE_NAME = "task"
        const val ID = "id"
        const val TITLE = "title"
        const val DESCRIPTION = "description"
        const val IS_COMPLETED = "is_completed"
    }
}