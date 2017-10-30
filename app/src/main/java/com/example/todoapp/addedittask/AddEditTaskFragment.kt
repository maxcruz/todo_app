package com.example.todoapp.addedittask

import android.app.Activity
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.todoapp.R
import kotlinx.android.synthetic.main.addtask_activity.*
import kotlinx.android.synthetic.main.addtask_fragment.*

class AddEditTaskFragment : Fragment(), AddEditTaskContract.View {

    companion object {

        const val ARGUMENT_EDIT_TASK_ID = "editTaskId"

        fun newInstance(): AddEditTaskFragment {
            return AddEditTaskFragment()
        }

    }

    private var mPresenter: AddEditTaskContract.Presenter? = null

    override fun onResume() {
        super.onResume()
        mPresenter?.start()
    }

    override fun setPresenter(presenter: AddEditTaskContract.Presenter) {
        mPresenter = presenter
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.addtask_fragment, container, false)
        setHasOptionsMenu(true)
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        activity?.fabAddTask?.setImageResource(R.drawable.ic_done)
        activity?.fabAddTask?.setOnClickListener {
            mPresenter?.saveTask(addTaskTitle?.text.toString(), addTaskDescription?.text.toString())
        }
    }

    override fun isActive() = isAdded

    override fun showEmptyTaskError() {
        val title = addTaskTitle ?: return
        Snackbar.make(title, getString(R.string.empty_task_message), Snackbar.LENGTH_LONG).show()
    }

    override fun showTasksList() {
        activity?.setResult(Activity.RESULT_OK)
        activity?.finish()
    }

    override fun setTitle(title: String) {
        addTaskTitle?.setText(title)
    }

    override fun setDescription(description: String) {
        addTaskDescription?.setText(description)
    }

}
