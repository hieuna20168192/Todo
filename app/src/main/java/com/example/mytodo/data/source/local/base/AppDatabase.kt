package com.example.mytodo.data.source.local.base

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.mytodo.data.model.Task

class AppDatabase private constructor(context: Context, dbName: String, version: Int) :
        SQLiteOpenHelper(context, dbName, null, version) {

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(CREATE_TABLE_TASK)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL(DROP_TABLE_TASK)
        onCreate(db)
    }

    companion object {
        private const val NAME = "todo.db"
        private const val VERSION = 1

        private const val CREATE_TABLE_TASK = "CREATE TABLE " + Task.TABLE_NAME + "(" +
                Task.ID + " TEXT PRIMARY KEY, "+
                Task.TITLE + " TEXT, " +
                Task.DESCRIPTION + " TEXT, " +
                Task.IS_COMPLETED + " INTEGER)"

        private const val DROP_TABLE_TASK = "DROP TABLE IF EXISTS " + Task.TABLE_NAME

        private val lock = Any()

        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase =
            instance ?: synchronized(lock) {
                instance ?: AppDatabase(context, NAME, VERSION).also {
                    instance = it
                }
            }
    }
}

