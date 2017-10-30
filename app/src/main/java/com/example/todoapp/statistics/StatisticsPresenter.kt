package com.example.todoapp.statistics

import com.example.todoapp.data.Task
import com.example.todoapp.data.source.TasksDataSource
import com.example.todoapp.data.source.TasksRepository

class StatisticsPresenter(private val mTasksRepository: TasksRepository,
                          private val mStatisticsView: StatisticsContract.View) :
        StatisticsContract.Presenter {

    init {
        mStatisticsView.setPresenter(this)
    }

    override fun start() {
        loadStatistics()
    }

    private fun loadStatistics() {
        mStatisticsView.setProgressIndicator(true)
        mTasksRepository.getTasks(object : TasksDataSource.LoadTasksCallback {

            override fun onTasksLoaded(tasks: List<Task>) {
                var activeTasks = 0
                var completedTasks = 0
                for (task in tasks) {
                    if (task.isCompleted) {
                        completedTasks += 1
                    } else {
                        activeTasks += 1
                    }
                }
                if (!mStatisticsView.isActive()) {
                    return
                }
                mStatisticsView.setProgressIndicator(false)
                mStatisticsView.showStatistics(activeTasks, completedTasks)
            }

            override fun onDataNotAvailable() {
                if (!mStatisticsView.isActive()) {
                    return
                }
                mStatisticsView.showLoadingStatisticsError()
            }

        })
    }

}
