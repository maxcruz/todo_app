package com.example.todoapp.statistics

import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.NavUtils
import android.support.v4.view.GravityCompat
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.example.todoapp.Injection
import com.example.todoapp.R
import com.example.todoapp.util.addFragmentToActivity
import kotlinx.android.synthetic.main.statistics_activity.*

class StatisticsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.statistics_activity)
        setToolbar()
        setNavigationView()
        val view = getStatisticsView()
        setPresenter(view)
    }

    private fun setPresenter(view: StatisticsContract.View) {
        StatisticsPresenter(Injection.provideTasksRepository(applicationContext), view)
    }

    private fun getStatisticsView(): StatisticsContract.View {
        var statisticsFragment = supportFragmentManager.findFragmentById(R.id.contentFrame)
                as StatisticsFragment?
        if (statisticsFragment == null) {
            statisticsFragment = StatisticsFragment.newInstance()
            addFragmentToActivity(statisticsFragment, R.id.contentFrame)
        }
        return statisticsFragment
    }

    private fun setNavigationView() {
        drawerLayout?.setStatusBarBackground(R.color.colorPrimaryDark)
        if (navigationView != null) {
            setupDrawerContent(navigationView)
        }
    }

    private fun setToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setTitle(R.string.statistics_title)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_menu)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                drawerLayout?.openDrawer(GravityCompat.START)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupDrawerContent(navigationView: NavigationView) {
        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.list_navigation_menu_item -> {
                    NavUtils.navigateUpFromSameTask(this@StatisticsActivity)
                }
                R.id.statistics_navigation_menu_item -> {
                }
                else -> {
                }
            }
            menuItem.isChecked = true
            drawerLayout?.closeDrawers()
            true
        }
    }
}
