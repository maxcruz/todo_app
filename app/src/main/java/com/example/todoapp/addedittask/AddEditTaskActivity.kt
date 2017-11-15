package com.example.todoapp.addedittask

import android.os.Bundle
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import com.example.todoapp.Injection
import com.example.todoapp.R
import com.example.todoapp.data.source.TasksRepository
import com.example.todoapp.util.addFragmentToActivity
import kotlinx.android.synthetic.main.addtask_activity.*

class AddEditTaskActivity : AppCompatActivity() {

    companion object {

        const val REQUEST_ADD_TASK = 1
        const val SHOULD_LOAD_DATA_KEY = "shouldLoadData"

    }

    private var mAddEditTaskPresenter: AddEditTaskPresenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.addtask_activity)
        val taskId = getTaskIdentifier()
        val repository = getTaskRepository()
        val view = getAddEditTaskView(taskId)
        setToolbar(taskId)
        setPresenter(savedInstanceState, taskId, repository, view)
    }

    fun setPresenter(savedInstanceState: Bundle?, taskId: String?,
                             repository: TasksRepository, view: AddEditTaskContract.View) {
        var shouldLoadData = true
        if (savedInstanceState != null) {
            shouldLoadData = savedInstanceState.getBoolean(SHOULD_LOAD_DATA_KEY)
        }
        mAddEditTaskPresenter = AddEditTaskPresenter(taskId, repository, view, shouldLoadData)
    }

    private fun getTaskRepository(): TasksRepository {
        return Injection.provideTasksRepository(applicationContext)
    }

    private fun getAddEditTaskView(taskId: String?): AddEditTaskContract.View {
        var addEditTaskFragment = supportFragmentManager.findFragmentById(R.id.contentFrame)
                as AddEditTaskFragment?
        if (addEditTaskFragment == null) {
            addEditTaskFragment = AddEditTaskFragment.newInstance()
            if (intent.hasExtra(AddEditTaskFragment.ARGUMENT_EDIT_TASK_ID)) {
                val bundle = Bundle()
                bundle.putString(AddEditTaskFragment.ARGUMENT_EDIT_TASK_ID, taskId)
                addEditTaskFragment.arguments = bundle
            }
            addFragmentToActivity(addEditTaskFragment, R.id.contentFrame)
        }
        return addEditTaskFragment
    }

    private fun setToolbar(taskId: String?) {
        setSupportActionBar(toolbar)
        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setDisplayShowHomeEnabled(true)
        setToolbarTitle(actionBar, taskId)
    }

    private fun getTaskIdentifier(): String? {
        return intent.getStringExtra(AddEditTaskFragment.ARGUMENT_EDIT_TASK_ID)
    }

    private fun setToolbarTitle(actionBar: ActionBar?, taskId: String?) {
        if (taskId == null) {
            actionBar?.setTitle(R.string.add_task)
        } else {
            actionBar?.setTitle(R.string.edit_task)
        }
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        val presenter = mAddEditTaskPresenter
        if (presenter != null) {
            outState?.putBoolean(SHOULD_LOAD_DATA_KEY, presenter.isDataMissing())
        }
        super.onSaveInstanceState(outState)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}
