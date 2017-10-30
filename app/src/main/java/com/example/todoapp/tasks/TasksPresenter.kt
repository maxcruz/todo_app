package com.example.todoapp.tasks

import android.app.Activity
import com.example.todoapp.addedittask.AddEditTaskActivity
import com.example.todoapp.data.Task
import com.example.todoapp.data.source.TasksDataSource
import java.util.*

class TasksPresenter(private val mTasksRepository: TasksDataSource,
                     private val mTasksView: TasksContract.View) : TasksContract.Presenter {

    private var mCurrentFiltering = TasksFilterType.ALL_TASKS
    private var mFirstLoad = true

    init {
        mTasksView.setPresenter(this)
    }

    override fun start() {
        loadTasks(false)
    }

    override fun result(requestCode: Int, resultCode: Int) {
        if (AddEditTaskActivity.REQUEST_ADD_TASK == requestCode && Activity.RESULT_OK == resultCode) {
            mTasksView.showSuccessfullySavedMessage()
        }
    }

    override fun loadTasks(forceUpdate: Boolean) {
        loadTasks(forceUpdate || mFirstLoad, true)
        mFirstLoad = false
    }

    private fun loadTasks(forceUpdate: Boolean, showLoadingUI: Boolean) {
        if (showLoadingUI) {
            mTasksView.setLoadingIndicator(true)
        }
        if (forceUpdate) {
            mTasksRepository.refreshTasks()
        }
        mTasksRepository.getTasks(object : TasksDataSource.LoadTasksCallback {

            override fun onTasksLoaded(tasks: List<Task>) {
                val tasksToShow = ArrayList<Task>()
                for (task in tasks) {
                    when (mCurrentFiltering) {
                        TasksFilterType.ALL_TASKS -> tasksToShow.add(task)
                        TasksFilterType.ACTIVE_TASKS -> if (task.isActive) {
                            tasksToShow.add(task)
                        }
                        TasksFilterType.COMPLETED_TASKS -> if (task.isCompleted) {
                            tasksToShow.add(task)
                        }
                    }
                }
                if (!mTasksView.isActive()) {
                    return
                }
                if (showLoadingUI) {
                    mTasksView.setLoadingIndicator(false)
                }
                processTasks(tasksToShow)
            }

            override fun onDataNotAvailable() {
                if (!mTasksView.isActive()) {
                    return
                }
                mTasksView.setLoadingIndicator(false)
                mTasksView.showLoadingTasksError()
            }

        })
    }

    private fun processTasks(tasks: List<Task>) {
        if (tasks.isEmpty()) {
            processEmptyTasks()
        } else {
            mTasksView.showTasks(tasks)
            showFilterLabel()
        }
    }

    private fun showFilterLabel() {
        when (mCurrentFiltering) {
            TasksFilterType.ACTIVE_TASKS -> mTasksView.showActiveFilterLabel()
            TasksFilterType.COMPLETED_TASKS -> mTasksView.showCompletedFilterLabel()
            else -> mTasksView.showAllFilterLabel()
        }
    }

    private fun processEmptyTasks() {
        when (mCurrentFiltering) {
            TasksFilterType.ACTIVE_TASKS -> mTasksView.showNoActiveTasks()
            TasksFilterType.COMPLETED_TASKS -> mTasksView.showNoCompletedTasks()
            else -> mTasksView.showNoTasks()
        }
    }

    override fun addNewTask() {
        mTasksView.showAddTask()
    }

    override fun openTaskDetails(requestedTask: Task) {
        mTasksView.showTaskDetailsUi(requestedTask.id)
    }

    override fun completeTask(completedTask: Task) {
        mTasksRepository.completeTask(completedTask)
        mTasksView.showTaskMarkedComplete()
        loadTasks(false, false)
    }

    override fun activateTask(activeTask: Task) {
        mTasksRepository.activateTask(activeTask)
        mTasksView.showTaskMarkedActive()
        loadTasks(false, false)
    }

    override fun clearCompletedTasks() {
        mTasksRepository.clearCompletedTasks()
        mTasksView.showCompletedTasksCleared()
        loadTasks(false, false)
    }

    override fun setFiltering(requestType: TasksFilterType) {
        mCurrentFiltering = requestType
    }

    override fun getFiltering(): TasksFilterType {
        return mCurrentFiltering
    }

}
