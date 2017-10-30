package com.example.todoapp.taskdetail

import com.example.todoapp.data.Task
import com.example.todoapp.data.source.TasksDataSource
import com.example.todoapp.data.source.TasksRepository

class TaskDetailPresenter(private val mTaskId: String?,
                          private val mTasksRepository: TasksRepository,
                          private val mTaskDetailView: TaskDetailContract.View) :
        TaskDetailContract.Presenter {

    init {
        mTaskDetailView.setPresenter(this)
    }

    override fun start() {
        openTask()
    }

    private fun openTask() {
        val taskId = mTaskId
        if (taskId == null) {
            mTaskDetailView.showMissingTask()
            return
        }
        mTaskDetailView.setLoadingIndicator(true)
        mTasksRepository.getTask(taskId, object : TasksDataSource.GetTaskCallback {

            override fun onTaskLoaded(task: Task?) {
                if (!mTaskDetailView.isActive()) {
                    return
                }
                mTaskDetailView.setLoadingIndicator(false)
                if (null == task) {
                    mTaskDetailView.showMissingTask()
                } else {
                    showTask(task)
                }
            }

            override fun onDataNotAvailable() {
                if (!mTaskDetailView.isActive()) {
                    return
                }
                mTaskDetailView.showMissingTask()
            }

        })
    }

    override fun editTask() {
        val taskId = mTaskId
        if (taskId == null) {
            mTaskDetailView.showMissingTask()
            return
        }
        mTaskDetailView.showEditTask(taskId)
    }

    override fun deleteTask() {
        val taskId = mTaskId
        if (taskId == null) {
            mTaskDetailView.showMissingTask()
            return
        }
        mTasksRepository.deleteTask(taskId)
        mTaskDetailView.showTaskDeleted()
    }

    override fun completeTask() {
        val taskId = mTaskId
        if (taskId == null) {
            mTaskDetailView.showMissingTask()
            return
        }
        mTasksRepository.completeTask(taskId)
        mTaskDetailView.showTaskMarkedComplete()
    }

    override fun activateTask() {
        val taskId = mTaskId
        if (taskId == null) {
            mTaskDetailView.showMissingTask()
            return
        }
        mTasksRepository.activateTask(taskId)
        mTaskDetailView.showTaskMarkedActive()
    }

    private fun showTask(task: Task) {
        val title = task.title
        val description = task.description
        if (title == null) {
            mTaskDetailView.hideTitle()
        } else {
            mTaskDetailView.showTitle(title)
        }
        if (description == null) {
            mTaskDetailView.hideDescription()
        } else {
            mTaskDetailView.showDescription(description)
        }
        mTaskDetailView.showCompletionStatus(task.isCompleted)
    }

}
