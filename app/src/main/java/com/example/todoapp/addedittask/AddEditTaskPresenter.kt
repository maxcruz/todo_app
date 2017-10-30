package com.example.todoapp.addedittask

import com.example.todoapp.data.Task
import com.example.todoapp.data.source.TasksDataSource
import com.example.todoapp.exceptions.PopulateNewTaskException
import com.example.todoapp.exceptions.UpdateNewTaskException

class AddEditTaskPresenter(private val mTaskId: String?,
                           private val mTasksRepository: TasksDataSource,
                           private val mAddTaskView: AddEditTaskContract.View,
                           shouldLoadDataFromRepo: Boolean) :
        AddEditTaskContract.Presenter, TasksDataSource.GetTaskCallback {

    private var mIsDataMissing: Boolean = false
    private val isNewTask: Boolean get() = mTaskId == null

    init {
        mIsDataMissing = shouldLoadDataFromRepo
        mAddTaskView.setPresenter(this)
    }

    override fun start() {
        if (!isNewTask && mIsDataMissing) {
            populateTask()
        }
    }

    override fun saveTask(title: String, description: String) {
        if (isNewTask) {
            createTask(title, description)
        } else {
            updateTask(title, description)
        }
    }

    override fun populateTask() {
        if (isNewTask) throw PopulateNewTaskException()
        val taskId = mTaskId ?: throw PopulateNewTaskException()
        mTasksRepository.getTask(taskId, this)
    }

    override fun onTaskLoaded(task: Task?) {
        if (mAddTaskView.isActive()) {
            val title = task?.title
            val description = task?.description
            if (title != null) mAddTaskView.setTitle(title)
            if (description != null) mAddTaskView.setDescription(description)
        }
        mIsDataMissing = false
    }

    override fun isDataMissing(): Boolean {
        return mIsDataMissing
    }

    override fun onDataNotAvailable() {
        if (mAddTaskView.isActive()) {
            mAddTaskView.showEmptyTaskError()
        }
    }

    private fun createTask(title: String, description: String) {
        val newTask = Task(title, description)
        if (newTask.isEmpty) {
            mAddTaskView.showEmptyTaskError()
        } else {
            mTasksRepository.saveTask(newTask)
            mAddTaskView.showTasksList()
        }
    }

    private fun updateTask(title: String, description: String) {
        if (isNewTask) throw UpdateNewTaskException()
        if (mTaskId == null) throw UpdateNewTaskException()
        mTasksRepository.saveTask(Task(title, description, mTaskId))
        mAddTaskView.showTasksList()
    }

}
