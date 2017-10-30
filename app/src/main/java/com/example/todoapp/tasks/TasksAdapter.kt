package com.example.todoapp.tasks

import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.example.todoapp.R
import com.example.todoapp.data.Task
import kotlinx.android.synthetic.main.task_item.view.*

class TasksAdapter(tasks: List<Task>, private val mItemListener: TaskItemListener) : BaseAdapter() {

    private lateinit var mTasks: List<Task>

    init {
        setList(tasks)
    }

    fun replaceData(tasks: List<Task>) {
        setList(tasks)
        notifyDataSetChanged()
    }

    private fun setList(tasks: List<Task>) {
        mTasks = tasks
    }

    override fun getCount(): Int {
        return mTasks.size
    }

    override fun getItem(i: Int): Task {
        return mTasks[i]
    }

    override fun getItemId(i: Int): Long {
        return i.toLong()
    }

    override fun getView(i: Int, view: View?, viewGroup: ViewGroup): View? {
        val rowView: View
        rowView = if (view == null) {
            val inflater = LayoutInflater.from(viewGroup.context)
            inflater.inflate(R.layout.task_item, viewGroup, false)
        } else view
        val task = getItem(i)
        rowView.itemTitle.text = task.titleForList
        rowView.itemComplete.isChecked = task.isCompleted
        if (task.isCompleted) {
            rowView.background = ContextCompat.getDrawable(viewGroup.context, R.drawable.list_completed_touch_feedback)
        } else {
            rowView.background = ContextCompat.getDrawable(viewGroup.context, R.drawable.touch_feedback)
        }
        rowView.itemComplete.setOnClickListener {
            if (!task.isCompleted) {
                mItemListener.onCompleteTaskClick(task)
            } else {
                mItemListener.onActivateTaskClick(task)
            }
        }
        rowView.setOnClickListener { mItemListener.onTaskClick(task) }
        return rowView
    }

}