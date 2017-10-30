package com.example.todoapp.tasks

import com.example.todoapp.BaseView
import com.example.todoapp.data.Task
import com.example.todoapp.BasePresenter

interface TasksContract {

    interface View : BaseView<Presenter> {
        fun isActive(): Boolean
        fun setLoadingIndicator(active: Boolean)
        fun showTasks(tasks: List<Task>)
        fun showAddTask()
        fun showTaskDetailsUi(taskId: String)
        fun showTaskMarkedComplete()
        fun showTaskMarkedActive()
        fun showCompletedTasksCleared()
        fun showLoadingTasksError()
        fun showNoTasks()
        fun showActiveFilterLabel()
        fun showCompletedFilterLabel()
        fun showAllFilterLabel()
        fun showNoActiveTasks()
        fun showNoCompletedTasks()
        fun showSuccessfullySavedMessage()
        fun showFilteringPopUpMenu()
    }

    interface Presenter : BasePresenter {
        fun setFiltering(requestType: TasksFilterType)
        fun getFiltering(): TasksFilterType
        fun result(requestCode: Int, resultCode: Int)
        fun loadTasks(forceUpdate: Boolean)
        fun addNewTask()
        fun openTaskDetails(requestedTask: Task)
        fun completeTask(completedTask: Task)
        fun activateTask(activeTask: Task)
        fun clearCompletedTasks()
    }

}
