package com.example.mytodo.ui.main

import android.os.Bundle
import android.view.MenuItem
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.mytodo.Injection
import com.example.mytodo.R
import com.example.mytodo.ui.base.BaseActivity
import com.example.mytodo.ui.tasks.TasksFilterType
import com.example.mytodo.ui.tasks.TasksFragment
import com.example.mytodo.ui.tasks.TasksPresenter
import com.example.mytodo.utils.replaceFragment
import com.example.mytodo.utils.setupActionBar
import com.google.android.material.navigation.NavigationView


class TasksActivity : BaseActivity() {

    private val FILTERING_KEY = "CURRENT_FILTERING_KEY"

    private lateinit var drawerLayout: DrawerLayout

    private lateinit var tasksPresenter: TasksPresenter

    override val layoutResource: Int
        get() = R.layout.tasks_act

    override fun initComponent() {
        // Set up the toolbar
        setupActionBar(R.id.toolbar) {
            setHomeAsUpIndicator(R.drawable.ic_menu)
            setDisplayHomeAsUpEnabled(true)
        }

        // Set up the navigation drawer.
        drawerLayout = findViewById<DrawerLayout>(R.id.drawer_layout).apply {
            setStatusBarBackground(R.color.colorPrimaryDark)
        }
        setupDrawerContent(findViewById(R.id.nav_view))

    }

    override fun initData(savedInstanceState: Bundle?) {

        val tasksFragment = supportFragmentManager.findFragmentById(R.id.contentFrame)
                as TasksFragment? ?: TasksFragment.newInstance().also {
            replaceFragment(R.id.contentFrame, it)
        }

        // Create the presenter
        tasksPresenter = TasksPresenter(
            Injection.provideTasksRepository(applicationContext),
            tasksFragment
        ).apply {
            // Load previously saved state, if available
            if (savedInstanceState != null) {
                currentFiltering = savedInstanceState.getSerializable(FILTERING_KEY)
                        as TasksFilterType
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState.apply {
            putSerializable(FILTERING_KEY, tasksPresenter.currentFiltering)
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == android.R.id.home) {
            // Open the navigation drawer when the home icon is selected from the toolbar.
            drawerLayout.openDrawer(GravityCompat.START)
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    private fun setupDrawerContent(navigationView: NavigationView) {
        navigationView.setNavigationItemSelectedListener { menuItem ->
            if (menuItem.itemId == R.id.statistics_navigation_menu_item) {
                // Intent to statistics activity
            }
            // Close the navigation drawer when an item is selected
            menuItem.isChecked = true
            drawerLayout.closeDrawers()
            true
        }
    }
}