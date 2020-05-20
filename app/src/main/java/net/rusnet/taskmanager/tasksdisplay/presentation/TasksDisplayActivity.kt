package net.rusnet.taskmanager.tasksdisplay.presentation

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import net.rusnet.taskmanager.R
import net.rusnet.taskmanager.commons.app.injector

class TasksDisplayActivity : AppCompatActivity() {

    companion object {
        const val REQUEST_CODE_SAVE_TASK = 1
    }

    private val viewModel by lazy {
        ViewModelProvider(
            this,
            injector.taskDisplayViewModelFactory
        ).get(TasksDisplayViewModel::class.java)
    }
    private val toolbar by lazy { findViewById<Toolbar>(R.id.toolbar) }
    private val navigationView by lazy { findViewById<NavigationView>(R.id.navigation_view) }
    private val drawerLayout by lazy { findViewById<DrawerLayout>(R.id.drawer_layout) }
    private val taskCountViewMap by lazy {
        TasksDisplayState.values().map {
            it.navigationViewMenuId to
                    navigationView
                        .menu
                        .findItem(it.navigationViewMenuId)
                        .actionView
                        .findViewById<TextView>(R.id.text_view_task_count)
        }.toMap()
    }
    private val addButton by lazy { findViewById<FloatingActionButton>(R.id.button_add_task) }
    private val recyclerView by lazy { findViewById<RecyclerView>(R.id.recycler_view_tasks) }
    private val adapter by lazy {
        TasksAdapter(mutableListOf(), object : TasksAdapter.OnItemClickListener {
            override fun onClick(taskId: Long) {
                viewModel.onTaskClick(taskId)
            }

            override fun onLongClick(taskId: Long) {
                viewModel.onTaskLongClick(taskId)
            }
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.tasks_display_activity)

        initToolbar()
        initNavigationDrawer()
        addButton.setOnClickListener { viewModel.onAddButtonClicked() }
        initRecyclerView()
        initStateObservation()
        initTasksObservation()
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CODE_SAVE_TASK && resultCode == Activity.RESULT_OK) {
            viewModel.onPositiveResultFromEditActivity()
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun initToolbar() {
        toolbar.setNavigationIcon(R.drawable.ic_menu_white)
        setSupportActionBar(toolbar)
    }

    private fun initNavigationDrawer() {
        navigationView.setNavigationItemSelectedListener { menuItem ->
            viewModel.onDrawerItemSelected(menuItem.itemId)
            drawerLayout.closeDrawers()
            return@setNavigationItemSelectedListener true
        }

        viewModel.currentTaskCount.observe(this, Observer { countMap ->
            for ((menuItemId, count) in countMap) {
                taskCountViewMap[menuItemId]?.text = count
            }
        })
    }

    private fun initRecyclerView() {
        recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        recyclerView.adapter = adapter
    }

    private fun initStateObservation() {
        viewModel.currentTasksDisplayState.observe(this, Observer { newState ->
            supportActionBar?.title = getString(newState.toolbarTitle)
            navigationView.setCheckedItem(newState.navigationViewMenuId)
            addButton.visibility = newState.addButtonVisibility
        })
    }

    private fun initTasksObservation() {
        viewModel.currentViewTasks.observe(this, Observer { updatedViewTasksList ->
            adapter.viewTasksList.clear()
            adapter.viewTasksList.addAll(updatedViewTasksList)
            adapter.notifyDataSetChanged()
        })
    }

}
