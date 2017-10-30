package com.example.todoapp.tasks

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.example.todoapp.Injection
import com.example.todoapp.R
import com.example.todoapp.statistics.StatisticsActivity
import com.example.todoapp.util.addFragmentToActivity
import kotlinx.android.synthetic.main.tasks_activity.*

class TasksActivity : AppCompatActivity() {

    companion object {

        private const val CURRENT_FILTERING_KEY = "currentFiltering"

    }

    private var mTasksPresenter: TasksPresenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.tasks_activity)
        setToolbar()
        setNavigationView()
        val view = getTasksView()
        setPresenter(savedInstanceState, view)
    }

    private fun setPresenter(savedInstanceState: Bundle?, view: TasksContract.View) {
        mTasksPresenter = TasksPresenter(Injection.provideTasksRepository(applicationContext), view)
        if (savedInstanceState != null) {
            val currentFiltering = savedInstanceState.getSerializable(CURRENT_FILTERING_KEY)
                    as TasksFilterType
            mTasksPresenter?.setFiltering(currentFiltering)
        }
    }

    private fun getTasksView(): TasksContract.View {
        var tasksFragment = supportFragmentManager.findFragmentById(R.id.contentFrame)
                as TasksFragment?
        if (tasksFragment == null) {
            tasksFragment = TasksFragment.newInstance()
            addFragmentToActivity(tasksFragment, R.id.contentFrame)
        }
        return tasksFragment
    }

    private fun setNavigationView() {
        drawerLayout?.setStatusBarBackground(R.color.colorPrimaryDark)
        if (navigationView != null) {
            setupDrawerContent(navigationView)
        }
    }

    private fun setToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_menu)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    public override fun onSaveInstanceState(outState: Bundle?) {
        outState!!.putSerializable(CURRENT_FILTERING_KEY, mTasksPresenter!!.getFiltering())

        super.onSaveInstanceState(outState)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                drawerLayout.openDrawer(GravityCompat.START)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupDrawerContent(navigationView: NavigationView) {
        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.list_navigation_menu_item -> { }
                R.id.statistics_navigation_menu_item -> {
                    val intent = Intent(this@TasksActivity, StatisticsActivity::class.java)
                    startActivity(intent)
                }
                else -> { }
            }
            menuItem.isChecked = true
            drawerLayout?.closeDrawers()
            true
        }
    }

}
