package net.rusnet.taskmanager.edit.presentation

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)

        initActionBar()
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
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun initEventObservation() {
        fun setToolbarTitle(newTitle: String) {
            supportActionBar?.title = newTitle
        }

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
                EditEvents.SetTitleForNewTask -> setToolbarTitle(getString(R.string.title_new_task))
                EditEvents.SetTitleForExistingTask -> setToolbarTitle(getString(R.string.title_existing_task))
                EditEvents.NavigateBack -> super.onBackPressed()
                EditEvents.ShowExitConfirmationDialog -> showExitConfirmationDialog()
            }.exhaustive
        })
    }

}
