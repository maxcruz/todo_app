package com.example.todoapp.tasks

import com.example.todoapp.data.Task

interface TaskItemListener {

    fun onTaskClick(clickedTask: Task)
    fun onCompleteTaskClick(completedTask: Task)
    fun onActivateTaskClick(activatedTask: Task)

}