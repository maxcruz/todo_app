package com.example.todoapp.addedittask

import com.example.todoapp.BasePresenter
import com.example.todoapp.BaseView

interface AddEditTaskContract {

    interface View : BaseView<Presenter> {

        fun isActive(): Boolean
        fun showEmptyTaskError()
        fun showTasksList()
        fun setTitle(title: String)
        fun setDescription(description: String)

    }

    interface Presenter : BasePresenter {

        fun isDataMissing(): Boolean
        fun saveTask(title: String, description: String)
        fun populateTask()

    }
}
