package com.example.todoapp.taskdetail

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.example.todoapp.Injection
import com.example.todoapp.R
import com.example.todoapp.util.addFragmentToActivity
import kotlinx.android.synthetic.main.taskdetail_activity.*

class TaskDetailActivity : AppCompatActivity() {

    companion object {

        const val EXTRA_TASK_ID = "taskId"

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.taskdetail_activity)
        setToolbar()
        val taskId = getTaskIdentifier()
        val view = getTaskDetailView(taskId)
        setPresenter(taskId, view)
    }

    private fun setPresenter(taskId: String?, taskDetailView: TaskDetailContract.View) {
        TaskDetailPresenter(
                taskId,
                Injection.provideTasksRepository(applicationContext),
                taskDetailView)
    }

    private fun getTaskDetailView(taskId: String?): TaskDetailContract.View {
        var taskDetailFragment = supportFragmentManager.findFragmentById(R.id.contentFrame)
                as TaskDetailFragment?
        if (taskDetailFragment == null) {
            taskDetailFragment = TaskDetailFragment.newInstance(taskId)
            addFragmentToActivity(taskDetailFragment, R.id.contentFrame)
        }
        return taskDetailFragment
    }

    private fun getTaskIdentifier(): String? {
        return intent.getStringExtra(EXTRA_TASK_ID)
    }

    private fun setToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}
