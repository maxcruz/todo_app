package com.example.todoapp.data.source.local

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context

import com.example.todoapp.data.Task
import com.example.todoapp.util.SingletonHolder

@Database(entities = arrayOf(Task::class), version = 1)
abstract class ToDoDatabase : RoomDatabase() {

    abstract fun taskDao(): TasksDao

    companion object : SingletonHolder<ToDoDatabase, Context>(
            { context ->
                val application = context.applicationContext
                val klass = ToDoDatabase::class.java
                Room.databaseBuilder(application, klass, "Tasks.db").build()
            }
    )

}
