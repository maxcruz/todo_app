package com.example.todoapp.data.source

import com.example.todoapp.data.Task
import com.example.todoapp.util.SingletonHolder
import java.util.*

open class TasksRepository private constructor(tasksLocalDataSource: TasksDataSource) : TasksDataSource {

    companion object : SingletonHolder<TasksRepository, TasksDataSource>(
            { tasksDataSource ->
                TasksRepository(tasksDataSource)
            }
    )

    private val mTasksLocalDataSource: TasksDataSource = tasksLocalDataSource
    private var mCachedTasks: MutableMap<String, Task>? = null
    private var mCacheIsDirty = false

    override fun getTasks(callback: TasksDataSource.LoadTasksCallback) {
        if (mCachedTasks != null && !mCacheIsDirty) {
            callback.onTasksLoaded(ArrayList(mCachedTasks?.values))
            return
        }
        mTasksLocalDataSource.getTasks(object : TasksDataSource.LoadTasksCallback {

            override fun onTasksLoaded(tasks: List<Task>) {
                refreshCache(tasks)
                callback.onTasksLoaded(ArrayList(mCachedTasks?.values))
            }

            override fun onDataNotAvailable() {
                callback.onDataNotAvailable()
            }

        })
    }

    override fun saveTask(task: Task) {
        mTasksLocalDataSource.saveTask(task)
        if (mCachedTasks == null) {
            mCachedTasks = LinkedHashMap()
        }
        mCachedTasks?.put(task.id, task)
    }

    override fun completeTask(task: Task) {
        mTasksLocalDataSource.completeTask(task)
        val completedTask = Task(task.id, task.title, task.description, true)
        if (mCachedTasks == null) {
            mCachedTasks = LinkedHashMap()
        }
        mCachedTasks?.put(task.id, completedTask)
    }

    override fun completeTask(taskId: String) {
        getTaskWithId(taskId)?.let { task -> completeTask(task) }
    }

    override fun activateTask(task: Task) {
        mTasksLocalDataSource.activateTask(task)
        val activeTask = Task(task.title, task.description, task.id)
        if (mCachedTasks == null) {
            mCachedTasks = LinkedHashMap()
        }
        mCachedTasks?.put(task.id, activeTask)
    }

    override fun activateTask(taskId: String) {
        getTaskWithId(taskId)?.let { task -> activateTask(task) }
    }

    override fun clearCompletedTasks() {
        mTasksLocalDataSource.clearCompletedTasks()
        if (mCachedTasks == null) {
            mCachedTasks = LinkedHashMap()
        }
        val it = mCachedTasks?.entries?.iterator()
        if (it != null)
            while (it.hasNext()) {
                val entry = it.next()
                if (entry.value.isCompleted) {
                    it.remove()
                }
            }
    }

    override fun getTask(taskId: String, callback: TasksDataSource.GetTaskCallback) {
        val cachedTask = getTaskWithId(taskId)
        if (cachedTask != null) {
            callback.onTaskLoaded(cachedTask)
            return
        }
        mTasksLocalDataSource.getTask(taskId, object : TasksDataSource.GetTaskCallback {

            override fun onTaskLoaded(task: Task?) {
                if (mCachedTasks == null) {
                    mCachedTasks = LinkedHashMap()
                }
                if (task != null) mCachedTasks?.put(task.id, task)
                callback.onTaskLoaded(task)
            }

            override fun onDataNotAvailable() {
                callback.onDataNotAvailable()
            }
        })
    }

    override fun refreshTasks() {
        mCacheIsDirty = true
    }

    override fun deleteAllTasks() {
        mTasksLocalDataSource.deleteAllTasks()
        if (mCachedTasks == null) {
            mCachedTasks = LinkedHashMap()
        }
        mCachedTasks?.clear()
    }

    override fun deleteTask(taskId: String) {
        mTasksLocalDataSource.deleteTask(taskId)
        mCachedTasks?.remove(taskId)
    }

    private fun refreshCache(tasks: List<Task>) {
        if (mCachedTasks == null) {
            mCachedTasks = LinkedHashMap()
        }
        mCachedTasks?.clear()
        for (task in tasks) {
            mCachedTasks?.put(task.id, task)
        }
        mCacheIsDirty = false
    }

    private fun getTaskWithId(id: String): Task? {
        val cachedTasks = mCachedTasks ?: return null
        if (cachedTasks.isEmpty()) return null
        return cachedTasks[id]
    }

    fun getCachedTasks(): MutableMap<String, Task>? {
        return mCachedTasks
    }

}
