package net.rusnet.taskmanager.edit.presentation

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import net.rusnet.taskmanager.R
import net.rusnet.taskmanager.commons.app.injector
import net.rusnet.taskmanager.commons.domain.model.TaskType
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

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onBackPressed() {
        showExitWithoutSavingDialog { super.onBackPressed() }
    }

    private fun showExitWithoutSavingDialog(onPositiveButtonPressed: () -> Unit) {
        // todo: show dialog only if any changes where made
        AlertDialog.Builder(this)
            .setTitle(R.string.exit_without_saving_warning)
            .setPositiveButton(R.string.yes) { _, _ ->
                onPositiveButtonPressed()
            }
            .setNegativeButton(R.string.no) { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun initActionBar() {
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title =
                if (intent.hasExtra(EXTRA_TASK)) getString(R.string.title_existing_task) else getString(
                    R.string.title_new_task
                )
        }
    }

}
