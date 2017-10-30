package com.example.todoapp.data.source.local

import com.example.todoapp.data.Task
import com.example.todoapp.data.source.TasksDataSource
import com.example.todoapp.util.AppExecutors
import com.example.todoapp.util.SingletonHolder

class TasksLocalDataSource private constructor(private val mAppExecutors: AppExecutors,
                                               private val mTasksDao: TasksDao) : TasksDataSource {

    override fun getTasks(callback: TasksDataSource.LoadTasksCallback) {
        val runnable = Runnable {
            val tasks = mTasksDao.tasks
            mAppExecutors.mainThread().execute {
                if (tasks.isEmpty()) {
                    callback.onDataNotAvailable()
                } else {
                    callback.onTasksLoaded(tasks)
                }
            }
        }
        mAppExecutors.diskIO().execute(runnable)
    }

    override fun getTask(taskId: String, callback: TasksDataSource.GetTaskCallback) {
        val runnable = Runnable {
            val task = mTasksDao.getTaskById(taskId)
            mAppExecutors.mainThread().execute {
                if (task != null) {
                    callback.onTaskLoaded(task)
                } else {
                    callback.onDataNotAvailable()
                }
            }
        }
        mAppExecutors.diskIO().execute(runnable)
    }

    override fun saveTask(task: Task) {
        checkNotNull(task)
        val saveRunnable = Runnable { mTasksDao.insertTask(task) }
        mAppExecutors.diskIO().execute(saveRunnable)
    }

    override fun completeTask(task: Task) {
        val completeRunnable = Runnable { mTasksDao.updateCompleted(task.id, true) }

        mAppExecutors.diskIO().execute(completeRunnable)
    }

    override fun completeTask(taskId: String) {
    }

    override fun activateTask(task: Task) {
        val activateRunnable = Runnable { mTasksDao.updateCompleted(task.id, false) }
        mAppExecutors.diskIO().execute(activateRunnable)
    }

    override fun activateTask(taskId: String) {
    }

    override fun clearCompletedTasks() {
        val clearTasksRunnable = Runnable { mTasksDao.deleteCompletedTasks() }
        mAppExecutors.diskIO().execute(clearTasksRunnable)
    }

    override fun refreshTasks() {
    }

    override fun deleteAllTasks() {
        val deleteRunnable = Runnable { mTasksDao.deleteTasks() }
        mAppExecutors.diskIO().execute(deleteRunnable)
    }

    override fun deleteTask(taskId: String) {
        val deleteRunnable = Runnable { mTasksDao.deleteTaskById(taskId) }
        mAppExecutors.diskIO().execute(deleteRunnable)
    }

    companion object : SingletonHolder<TasksLocalDataSource, Pair<AppExecutors, TasksDao>>(
            { (appExecutors, tasksDao) ->
                TasksLocalDataSource(appExecutors, tasksDao)
            }
    )

}
