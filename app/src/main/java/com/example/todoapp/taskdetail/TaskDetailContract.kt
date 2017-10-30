package com.example.todoapp.taskdetail

import com.example.todoapp.BasePresenter
import com.example.todoapp.BaseView

interface TaskDetailContract {

    interface View : BaseView<Presenter> {
        fun isActive(): Boolean
        fun setLoadingIndicator(active: Boolean)
        fun showMissingTask()
        fun hideTitle()
        fun showTitle(title: String)
        fun hideDescription()
        fun showDescription(description: String)
        fun showCompletionStatus(complete: Boolean)
        fun showEditTask(taskId: String)
        fun showTaskDeleted()
        fun showTaskMarkedComplete()
        fun showTaskMarkedActive()
    }

    interface Presenter : BasePresenter {
        fun editTask()
        fun deleteTask()
        fun completeTask()
        fun activateTask()
    }

}
