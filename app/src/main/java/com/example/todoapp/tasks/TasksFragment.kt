package com.example.todoapp.tasks

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.widget.PopupMenu
import android.view.*
import com.example.todoapp.R
import com.example.todoapp.addedittask.AddEditTaskActivity
import com.example.todoapp.data.Task
import com.example.todoapp.taskdetail.TaskDetailActivity
import kotlinx.android.synthetic.main.tasks_activity.*
import kotlinx.android.synthetic.main.tasks_fragment.*
import java.util.*

class TasksFragment : Fragment(), TasksContract.View {

    companion object {

        fun newInstance(): TasksFragment {
            return TasksFragment()
        }

    }

    private var mPresenter: TasksContract.Presenter? = null
    private var mListAdapter: TasksAdapter? = null
    private var mItemListener: TaskItemListener = object : TaskItemListener {

        override fun onTaskClick(clickedTask: Task) {
            mPresenter?.openTaskDetails(clickedTask)
        }

        override fun onCompleteTaskClick(completedTask: Task) {
            mPresenter?.completeTask(completedTask)
        }

        override fun onActivateTaskClick(activatedTask: Task) {
            mPresenter?.activateTask(activatedTask)
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mListAdapter = TasksAdapter(ArrayList(0), mItemListener)
    }

    override fun onResume() {
        super.onResume()
        mPresenter?.start()
    }

    override fun setPresenter(presenter: TasksContract.Presenter) {
        mPresenter = presenter
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        mPresenter?.result(requestCode, resultCode)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.tasks_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tasksList.adapter = mListAdapter
        noTasksAdd?.setOnClickListener { showAddTask() }
        activity?.fabAddTask?.setImageResource(R.drawable.ic_add)
        activity?.fabAddTask?.setOnClickListener { mPresenter?.addNewTask() }
        val context = activity as Context
        refreshLayout.setColorSchemeColors(
                ContextCompat.getColor(context, R.color.colorPrimary),
                ContextCompat.getColor(context, R.color.colorAccent),
                ContextCompat.getColor(context, R.color.colorPrimaryDark)
        )
        refreshLayout.setScrollUpChild(tasksList)
        refreshLayout.setOnRefreshListener { mPresenter?.loadTasks(false) }
        setHasOptionsMenu(true)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.menu_clear -> mPresenter?.clearCompletedTasks()
            R.id.menu_filter -> showFilteringPopUpMenu()
        }
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.tasks_fragment_menu, menu)
    }

    override fun showFilteringPopUpMenu() {
        val myContext = context ?: return
        val myActivity = activity ?: return
        val popup = PopupMenu(myContext, myActivity.findViewById(R.id.menu_filter))
        popup.menuInflater.inflate(R.menu.filter_tasks, popup.menu)
        popup.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.active -> mPresenter?.setFiltering(TasksFilterType.ACTIVE_TASKS)
                R.id.completed -> mPresenter?.setFiltering(TasksFilterType.COMPLETED_TASKS)
                else -> mPresenter?.setFiltering(TasksFilterType.ALL_TASKS)
            }
            mPresenter?.loadTasks(false)
            true
        }

        popup.show()
    }

    override fun setLoadingIndicator(active: Boolean) {
        if (view == null) {
            return
        }
        refreshLayout.post { refreshLayout.isRefreshing = active }
    }

    override fun showTasks(tasks: List<Task>) {
        mListAdapter?.replaceData(tasks)
        tasksLL?.visibility = View.VISIBLE
        noTasks?.visibility = View.GONE
    }

    override fun showNoActiveTasks() {
        showNoTasksViews(
                resources.getString(R.string.no_tasks_active),
                R.drawable.ic_check_circle_24dp,
                false
        )
    }

    override fun showNoTasks() {
        showNoTasksViews(
                resources.getString(R.string.no_tasks_all),
                R.drawable.ic_assignment_turned_in_24dp,
                false
        )
    }

    override fun showNoCompletedTasks() {
        showNoTasksViews(
                resources.getString(R.string.no_tasks_completed),
                R.drawable.ic_verified_user_24dp,
                false
        )
    }

    override fun showSuccessfullySavedMessage() {
        showMessage(getString(R.string.successfully_saved_task_message))
    }

    private fun showNoTasksViews(mainText: String, iconRes: Int, showAddView: Boolean) {
        tasksLL.visibility = View.GONE
        noTasks.visibility = View.VISIBLE
        noTasksMain.text = mainText
        noTasksIcon.setImageDrawable(ContextCompat.getDrawable(activity as Context, iconRes))
        noTasksAdd.visibility = if (showAddView) View.VISIBLE else View.GONE
    }

    override fun showActiveFilterLabel() {
        filteringLabel.text = resources.getString(R.string.label_active)
    }

    override fun showCompletedFilterLabel() {
        filteringLabel.text = resources.getString(R.string.label_completed)
    }

    override fun showAllFilterLabel() {
        filteringLabel.text = resources.getString(R.string.label_all)
    }

    override fun showAddTask() {
        val intent = Intent(context, AddEditTaskActivity::class.java)
        startActivityForResult(intent, AddEditTaskActivity.REQUEST_ADD_TASK)
    }

    override fun showTaskDetailsUi(taskId: String) {
        val intent = Intent(context, TaskDetailActivity::class.java)
        intent.putExtra(TaskDetailActivity.EXTRA_TASK_ID, taskId)
        startActivity(intent)
    }

    override fun showTaskMarkedComplete() {
        showMessage(getString(R.string.task_marked_complete))
    }

    override fun showTaskMarkedActive() {
        showMessage(getString(R.string.task_marked_active))
    }

    override fun showCompletedTasksCleared() {
        showMessage(getString(R.string.completed_tasks_cleared))
    }

    override fun showLoadingTasksError() {
        showMessage(getString(R.string.loading_tasks_error))
    }

    private fun showMessage(message: String) {
        val myView = view ?: return
        Snackbar.make(myView, message, Snackbar.LENGTH_LONG).show()
    }

    override fun isActive(): Boolean {
        return isAdded
    }

}