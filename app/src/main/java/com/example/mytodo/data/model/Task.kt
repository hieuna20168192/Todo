package com.example.mytodo.data.model

import android.content.ContentValues
import android.database.Cursor
import java.util.*

data class Task(
    val id: String = UUID.randomUUID().toString(),
    val title: String = UUID.randomUUID().toString(),
    val description: String = UUID.randomUUID().toString()
) {

    constructor(cursor: Cursor) : this(
        id = cursor.getString(cursor.getColumnIndex(ID)),
        title = cursor.getString(cursor.getColumnIndex(TITLE)),
        description = cursor.getString(cursor.getColumnIndex(DESCRIPTION))
    )

    fun getContentValues() = ContentValues().apply {
        put(ID, id)
        put(TITLE, title)
        put(DESCRIPTION, description)
    }
    companion object {
        const val TABLE_NAME = "task"
        const val ID = "id"
        const val TITLE = "title"
        const val DESCRIPTION = "description"
    }
}