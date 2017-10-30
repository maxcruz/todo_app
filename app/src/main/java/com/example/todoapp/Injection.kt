package com.example.todoapp

import android.content.Context
import com.example.todoapp.data.source.TasksRepository
import com.example.todoapp.data.source.local.TasksLocalDataSource
import com.example.todoapp.data.source.local.ToDoDatabase
import com.example.todoapp.util.AppExecutors

object Injection {

    fun provideTasksRepository(context: Context): TasksRepository {
        val database = ToDoDatabase.getInstance(context)
        val parameters = Pair(AppExecutors(), database.taskDao())
        val dataSource = TasksLocalDataSource.getInstance(parameters)
        return TasksRepository.getInstance(dataSource)
    }

}
