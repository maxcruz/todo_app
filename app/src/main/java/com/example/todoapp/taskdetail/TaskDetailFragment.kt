package com.example.todoapp.taskdetail

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.view.*
import com.example.todoapp.R
import com.example.todoapp.addedittask.AddEditTaskActivity
import com.example.todoapp.addedittask.AddEditTaskFragment
import kotlinx.android.synthetic.main.taskdetail_activity.*
import kotlinx.android.synthetic.main.taskdetail_fragment.*

class TaskDetailFragment : Fragment(), TaskDetailContract.View {

    companion object {

        private const val ARGUMENT_TASK_ID = "taskId"
        private const val REQUEST_EDIT_TASK = 1

        fun newInstance(taskId: String?): TaskDetailFragment {
            val arguments = Bundle()
            arguments.putString(ARGUMENT_TASK_ID, taskId)
            val fragment = TaskDetailFragment()
            fragment.arguments = arguments
            return fragment
        }

    }

    private var mPresenter: TaskDetailContract.Presenter? = null

    override fun onResume() {
        super.onResume()
        mPresenter?.start()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.taskdetail_fragment, container, false)
        setHasOptionsMenu(true)
        activity?.fabEditTask?.setOnClickListener {
            mPresenter?.editTask()
        }
        return root
    }

    override fun setPresenter(presenter: TaskDetailContract.Presenter) {
        mPresenter = presenter
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.menu_delete -> {
                mPresenter?.deleteTask()
                return true
            }
        }
        return false
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.taskdetail_fragment_menu, menu)
    }

    override fun setLoadingIndicator(active: Boolean) {
        if (active) {
            taskDetailTitle?.text = ""
            taskDetailDescription?.text = getString(R.string.loading)
        }
    }

    override fun hideDescription() {
        taskDetailDescription?.visibility = View.GONE
    }

    override fun hideTitle() {
        taskDetailTitle?.visibility = View.GONE
    }

    override fun showDescription(description: String) {
        taskDetailDescription?.visibility = View.VISIBLE
        taskDetailDescription?.text = description
    }

    override fun showCompletionStatus(complete: Boolean) {
        taskDetailComplete?.isChecked = complete
        taskDetailComplete?.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                mPresenter?.completeTask()
            } else {
                mPresenter?.activateTask()
            }
        }
    }

    override fun showEditTask(taskId: String) {
        val intent = Intent(context, AddEditTaskActivity::class.java)
        intent.putExtra(AddEditTaskFragment.ARGUMENT_EDIT_TASK_ID, taskId)
        startActivityForResult(intent, REQUEST_EDIT_TASK)
    }

    override fun showTaskDeleted() {
        activity?.finish()
    }

    override fun showTaskMarkedComplete() {
        val myView = view ?: return
        Snackbar.make(myView, getString(R.string.task_marked_complete), Snackbar.LENGTH_LONG)
                .show()
    }

    override fun showTaskMarkedActive() {
        val myView = view ?: return
        Snackbar.make(myView, getString(R.string.task_marked_active), Snackbar.LENGTH_LONG)
                .show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_EDIT_TASK) {
            if (resultCode == Activity.RESULT_OK) {
                activity?.finish()
            }
        }
    }

    override fun showTitle(title: String) {
        taskDetailTitle?.visibility = View.VISIBLE
        taskDetailTitle?.text = title
    }

    override fun showMissingTask() {
        taskDetailTitle?.text = ""
        taskDetailDescription?.text = getString(R.string.no_data)
    }

    override fun isActive(): Boolean = isAdded

}
