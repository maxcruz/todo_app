package com.example.todoapp.statistics

import com.example.todoapp.BasePresenter
import com.example.todoapp.BaseView

interface StatisticsContract {

    interface View : BaseView<Presenter> {

        fun isActive(): Boolean
        fun setProgressIndicator(active: Boolean)
        fun showStatistics(numberOfIncompleteTasks: Int, numberOfCompletedTasks: Int)
        fun showLoadingStatisticsError()

    }

    interface Presenter : BasePresenter

}
