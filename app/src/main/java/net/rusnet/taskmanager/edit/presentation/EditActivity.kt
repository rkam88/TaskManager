package net.rusnet.taskmanager.edit.presentation

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import net.rusnet.taskmanager.R
import net.rusnet.taskmanager.commons.app.injector
import net.rusnet.taskmanager.commons.domain.model.TaskType
import net.rusnet.taskmanager.commons.exhaustive
import net.rusnet.taskmanager_old.commons.domain.model.Task

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
    private val taskName by lazy { findViewById<EditText>(R.id.edit_text_task_name) }
    private val taskTypeSpinner by lazy { findViewById<Spinner>(R.id.spinner_task_type) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.edit_activity)

        initActionBar()
        initTaskName()
        initTaskTypeSpinner()
        initEventObservation()

    }

    override fun onSupportNavigateUp(): Boolean {
        viewModel.onBackPressed()
        return true
    }

    override fun onBackPressed() {
        viewModel.onBackPressed()
    }

    private fun initActionBar() {
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = resources.getString(viewModel.getToolbarTitleStringResId())
        }
    }

    private fun initTaskName() {
        taskName.setText(viewModel.currentTask.name)
        taskName.addTextChangedListener {
            object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    viewModel.onTaskNameChanged(s.toString())
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            }
        }
    }

    private fun initTaskTypeSpinner() {
        taskTypeSpinner.adapter = ArrayAdapter(
            this,
            R.layout.edit_spinner_display,
            TaskType.values().map { resources.getString(it.nameInUi) }
        ).apply { setDropDownViewResource(R.layout.edit_spinner_dropdown) }

        taskTypeSpinner.setSelection(viewModel.currentTask.type.spinnerPosition)

        taskTypeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                viewModel.onTasksTypeSelected(position)
            }
        }
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

}
