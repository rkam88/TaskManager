package net.rusnet.taskmanager.edit.presentation

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import net.rusnet.taskmanager.R
import net.rusnet.taskmanager.commons.app.injector
import net.rusnet.taskmanager.commons.domain.model.Task
import net.rusnet.taskmanager.commons.domain.model.TaskType
import net.rusnet.taskmanager.commons.extensions.doOnItemSelected
import net.rusnet.taskmanager.commons.extensions.exhaustive

class EditActivity : AppCompatActivity() {

    companion object {
        private const val EXTRA_TASK = "EXTRA_TASK"
        private const val EXTRA_TASK_TYPE = "EXTRA_TASK_TYPE"

        fun getIntentForNewTask(activity: Activity, newTaskType: TaskType): Intent {
            return Intent(activity, EditActivity::class.java).apply {
                putExtra(EXTRA_TASK_TYPE, newTaskType)
            }
        }

        fun getIntentForExistingTask(activity: Activity, existingTask: Task): Intent {
            return Intent(activity, EditActivity::class.java).apply {
                putExtra(EXTRA_TASK, existingTask)
            }
        }
    }

    private val viewModel by lazy {
        ViewModelProvider(
            this,
            injector.editViewModelFactory
        ).get(EditViewModel::class.java).apply {
            initViewModelFromIntent(
                intent.getSerializableExtra(EXTRA_TASK) as? Task,
                intent.getSerializableExtra(EXTRA_TASK_TYPE) as? TaskType
            )
        }
    }
    private val taskNameEditText by lazy { findViewById<EditText>(R.id.edit_text_task_name) }
    private val taskTypeSpinner by lazy { findViewById<Spinner>(R.id.spinner_task_type) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.edit_activity)

        initViews()
        initEventObservation()
        initStateObservation()

    }

    override fun onSupportNavigateUp(): Boolean {
        viewModel.onBackPressed()
        return true
    }

    override fun onBackPressed() {
        viewModel.onBackPressed()
    }

    private fun initViews() {
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        taskNameEditText.doAfterTextChanged { viewModel.onTaskNameChanged(it.toString()) }

        taskTypeSpinner.adapter = ArrayAdapter(
            this,
            R.layout.edit_spinner_display,
            TaskType.values().map { resources.getString(it.nameInUi) }
        ).apply { setDropDownViewResource(R.layout.edit_spinner_dropdown) }
        taskTypeSpinner.doOnItemSelected { viewModel.onTasksTypeSelected(it) }
    }

    private fun initEventObservation() {
        fun showExitConfirmationDialog() {
            AlertDialog.Builder(this)
                .setTitle(R.string.exit_without_saving_warning)
                .setPositiveButton(R.string.yes) { _, _ ->
                    super.onBackPressed()
                }
                .setNegativeButton(R.string.no) { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }

        viewModel.event.observe(this, Observer { event ->
            when (event) {
                EditEvents.NavigateBack -> super.onBackPressed()
                EditEvents.ShowExitConfirmationDialog -> showExitConfirmationDialog()
            }.exhaustive
        })
    }

    private fun initStateObservation() {
        viewModel.editViewState.observe(this, Observer { newState ->
            supportActionBar?.title = resources.getString(newState.toolbarTitleStringResId)
            if (taskNameEditText.text.toString() != newState.taskName) {
                taskNameEditText.setText(newState.taskName)
            }
            taskTypeSpinner.setSelection(newState.taskType.spinnerPosition)
        })
    }

}
