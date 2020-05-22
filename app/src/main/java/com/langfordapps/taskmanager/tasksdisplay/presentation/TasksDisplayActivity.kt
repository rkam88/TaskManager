package com.langfordapps.taskmanager.tasksdisplay.presentation

import android.app.Activity
import android.content.Intent
import android.graphics.Canvas
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ActionMode
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.langfordapps.taskmanager.R
import com.langfordapps.taskmanager.commons.app.injector
import com.langfordapps.taskmanager.commons.extensions.exhaustive
import com.langfordapps.taskmanager.commons.presentation.ConfirmationDialogFragment
import com.langfordapps.taskmanager.commons.presentation.ConfirmationDialogFragment.ConfirmationDialogListener

private const val NO_DRAG_DIRS = 0

class TasksDisplayActivity :
    AppCompatActivity(),
    ConfirmationDialogListener {

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
        BASE_TASK_DISPLAY_STATES.map {
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
    private var isSwipeEnabled = true
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
    private var currentActionMode: ActionMode? = null
    private var actionModeCallback: ActionMode.Callback = object : ActionMode.Callback {
        override fun onCreateActionMode(mode: ActionMode, menu: Menu): Boolean {
            mode.menuInflater.inflate(R.menu.tasks_display_action_menu, menu)
            return true
        }

        override fun onPrepareActionMode(mode: ActionMode, menu: Menu): Boolean {
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
            addButton.hide()
            return true
        }

        override fun onActionItemClicked(mode: ActionMode, item: MenuItem): Boolean {
            return when (item.itemId) {
                R.id.action_menu_delete -> {
                    viewModel.onDeleteClicked()
                    true
                }
                else -> false
            }
        }

        override fun onDestroyActionMode(mode: ActionMode) {
            viewModel.onDestroyActionMode()
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
            addButton.show()
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.tasks_display_activity)

        initToolbar()
        initNavigationDrawer()
        addButton.setOnClickListener { viewModel.onAddButtonClicked() }
        initRecyclerView()
        initEventObservation()
        initStateObservation()
        initTasksObservation()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                drawerLayout.openDrawer(GravityCompat.START)
                return true
            }
            R.id.delete_completed -> {
                viewModel.onDeleteCompletedTasksClicked()
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

    override fun onPositiveResponse(dialogTag: String) {
        viewModel.onPositiveResponse(dialogTag)
    }

    override fun onNegativeResponse(dialogTag: String) {
        viewModel.onNegativeResponse(dialogTag)
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
        addSwipeToCompleteTaskCallback()
    }

    private fun addSwipeToCompleteTaskCallback() {
        val helper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(NO_DRAG_DIRS, ItemTouchHelper.LEFT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ) = false

            override fun isItemViewSwipeEnabled() = isSwipeEnabled

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val taskId = adapter.getTaskIdOfItem(position)
                if (direction == ItemTouchHelper.LEFT) viewModel.onTaskSwipedLeft(taskId)
            }

            override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
                val foregroundView: View = (viewHolder as TasksAdapter.ViewHolder).taskItemLayout
                ItemTouchHelper.Callback.getDefaultUIUtil().clearView(foregroundView)
            }

            override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
                if (viewHolder != null) {
                    val foregroundView: View = (viewHolder as TasksAdapter.ViewHolder).taskItemLayout
                    ItemTouchHelper.Callback.getDefaultUIUtil().onSelected(foregroundView)
                }
            }

            override fun onChildDraw(
                c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder,
                dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean
            ) {
                val foregroundView: View = (viewHolder as TasksAdapter.ViewHolder).taskItemLayout
                ItemTouchHelper.Callback.getDefaultUIUtil()
                    .onDraw(c, recyclerView, foregroundView, dX, dY, actionState, isCurrentlyActive)
            }

            override fun onChildDrawOver(
                c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder?,
                dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean
            ) {
                val foregroundView: View = (viewHolder as TasksAdapter.ViewHolder).taskItemLayout
                ItemTouchHelper.Callback.getDefaultUIUtil()
                    .onDrawOver(c, recyclerView, foregroundView, dX, dY, actionState, isCurrentlyActive)
            }
        })

        helper.attachToRecyclerView(recyclerView)
    }

    private fun initEventObservation() {
        viewModel.event.observe(this, Observer { event ->
            when (event) {
                is TasksDisplayEvent.ShowConfirmationDialog -> {
                    ConfirmationDialogFragment.newInstance(
                        dialogTag = event.dialogTag,
                        dialogTitle = event.dialogTitle
                    ).show(supportFragmentManager, event.dialogTag)
                }
                TasksDisplayEvent.FinishActionMode -> currentActionMode?.finish()
            }.exhaustive
        })
    }

    private fun initStateObservation() {
        viewModel.currentTasksDisplayState.observe(this, Observer { newState ->
            supportActionBar?.title = getString(newState.toolbarTitle)
            toolbar.menu.clear()
            newState.toolbarMenu?.let { toolbar.inflateMenu(it) }
            navigationView.setCheckedItem(newState.navigationViewMenuId)
            addButton.visibility = newState.addButtonVisibility
            isSwipeEnabled = newState.isSwipeEnabled
            if (newState.isActionModeEnabled && currentActionMode == null) {
                currentActionMode = startSupportActionMode(actionModeCallback)
            } else if (!newState.isActionModeEnabled) {
                currentActionMode = null
            }
            currentActionMode?.title = newState.actionModeTitle
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
