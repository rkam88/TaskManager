package net.rusnet.taskmanager.tasksdisplay.presentation

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import net.rusnet.taskmanager.R
import net.rusnet.taskmanager.commons.app.injector

class TasksDisplayActivity : AppCompatActivity() {

    private val viewModel by lazy {
        ViewModelProvider(
            this,
            injector.taskDisplayViewModelFactory
        ).get(TasksDisplayViewModel::class.java)
    }
    private val toolbar by lazy { findViewById<Toolbar>(R.id.toolbar) }
    private val navigationView by lazy { findViewById<NavigationView>(R.id.navigation_view) }
    private val drawerLayout by lazy { findViewById<DrawerLayout>(R.id.drawer_layout) }
    private val addButton by lazy { findViewById<FloatingActionButton>(R.id.button_add_task) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.tasks_display_activity)

        initToolbar()
        initNavigationDrawer()
        addButton.setOnClickListener { viewModel.onAddButtonClicked() }
        initStateObservation()
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
    }

    private fun initStateObservation() {
        viewModel.currentTasksDisplayState.observe(this, Observer { newState ->
            supportActionBar?.title = getString(newState.toolbarTitle)
            navigationView.setCheckedItem(newState.navigationViewMenuId)
            addButton.visibility = newState.addButtonVisibility
        })
    }

}
