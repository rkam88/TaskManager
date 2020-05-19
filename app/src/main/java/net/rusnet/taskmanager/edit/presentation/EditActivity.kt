package net.rusnet.taskmanager.edit.presentation

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.format.DateFormat
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import net.rusnet.taskmanager.R
import net.rusnet.taskmanager.commons.app.injector
import net.rusnet.taskmanager.commons.domain.model.DateType
import net.rusnet.taskmanager.commons.domain.model.Task
import net.rusnet.taskmanager.commons.domain.model.TaskType
import net.rusnet.taskmanager.commons.extensions.doOnItemSelected
import net.rusnet.taskmanager.commons.extensions.exhaustive
import net.rusnet.taskmanager.edit.presentation.dialogs.DatePickerFragment
import net.rusnet.taskmanager.edit.presentation.dialogs.OnDatePickerResultListener
import net.rusnet.taskmanager.edit.presentation.dialogs.TimePickerFragment
import java.util.Calendar
import kotlin.math.roundToInt

private const val TAG_DATE_PICKER_FRAGMENT = "TAG_DATE_PICKER_FRAGMENT"
private const val TAG_TIME_PICKER_FRAGMENT = "TAG_TIME_PICKER_FRAGMENT"

class EditActivity : AppCompatActivity(),
                     OnDatePickerResultListener {

    companion object {
        private const val EXTRA_TASK = "EXTRA_TASK"
        private const val EXTRA_TASK_TYPE = "EXTRA_TASK_TYPE"
        private const val EXTRA_SHOW_DATES = "EXTRA_SHOW_DATES"

        fun getIntentForNewTask(activity: Activity, newTaskType: TaskType, showDates: Boolean): Intent {
            return Intent(activity, EditActivity::class.java).apply {
                putExtra(EXTRA_TASK_TYPE, newTaskType)
                putExtra(EXTRA_SHOW_DATES, showDates)
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
                intent.getSerializableExtra(EXTRA_TASK_TYPE) as? TaskType,
                intent.getBooleanExtra(EXTRA_SHOW_DATES, false)
            )
        }
    }
    private val taskNameEditText by lazy { findViewById<EditText>(R.id.edit_text_task_name) }
    private val taskTypeSpinner by lazy { findViewById<Spinner>(R.id.spinner_task_type) }
    private val addDateButton by lazy { findViewById<TextView>(R.id.text_view_add_date) }
    private val deleteDateButton by lazy { findViewById<ImageView>(R.id.image_view_delete_date) }
    private val startDateButton by lazy { findViewById<TextView>(R.id.text_view_start_date) }
    private val endDateButton by lazy { findViewById<TextView>(R.id.text_view_end_date) }
    private val startTimeButton by lazy { findViewById<TextView>(R.id.text_view_start_time) }
    private val endTimeButton by lazy { findViewById<TextView>(R.id.text_view_end_time) }
    private val dateAddedLayout by lazy { findViewById<ConstraintLayout>(R.id.layout_date_added) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.edit_activity)

        initViews()
        initEventObservation()
        initStateObservation()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.edit_toolbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.save -> {
            viewModel.onSaveClicked()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        viewModel.onBackPressed()
        return true
    }

    override fun onBackPressed() {
        viewModel.onBackPressed()
    }

    override fun onDateSet(dateType: DateType, newDate: Calendar) {
        viewModel.onDateSet(dateType, newDate)
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

        addDateButton.setOnClickListener { viewModel.onAddDatePressed() }
        deleteDateButton.setOnClickListener { viewModel.onDeleteDatePressed() }
        startDateButton.setOnClickListener { viewModel.onStartDateClicked() }
        endDateButton.setOnClickListener { viewModel.onEndDateClicker() }
        startTimeButton.setOnClickListener { viewModel.onStartTimeClicked() }
        endTimeButton.setOnClickListener { viewModel.onEndTimeClicker() }

        // programmatically set top margin for divider to consider icon size and vertical margin
        (findViewById<View>(R.id.divider_date).layoutParams as ConstraintLayout.LayoutParams).apply {
            val verticalMargin = resources.getDimension(R.dimen.edit_vertical_margin)
            val iconSize = resources.getDimension(R.dimen.edit_icon_size)
            goneTopMargin = (verticalMargin * 2 + iconSize).roundToInt()
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
                is EditEvents.ShowDatePickerDialog -> {
                    DatePickerFragment.newInstance(event.dateType, event.initialDialogDate)
                        .show(supportFragmentManager, TAG_DATE_PICKER_FRAGMENT)
                }
                is EditEvents.ShowTimePickerDialog -> {
                    TimePickerFragment.newInstance(event.dateType, event.initialDialogDate)
                        .show(supportFragmentManager, TAG_TIME_PICKER_FRAGMENT)
                }
                EditEvents.ShowNoTaskNameMessage -> {
                    Toast.makeText(this, R.string.message_empty_task_name, Toast.LENGTH_SHORT).show()
                }
                EditEvents.FinishActivityWithPositiveResult -> {
                    setResult(Activity.RESULT_OK)
                    finish()
                }
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
            addDateButton.visibility = if (newState.showDates) GONE else VISIBLE
            dateAddedLayout.visibility = if (newState.showDates) VISIBLE else GONE
            startDateButton.text = DateFormat.getDateFormat(this).format(newState.startDate)
            startTimeButton.text = DateFormat.getTimeFormat(this).format(newState.startDate)
            endDateButton.text = DateFormat.getDateFormat(this).format(newState.endDate)
            endTimeButton.text = DateFormat.getTimeFormat(this).format(newState.endDate)
        })
    }

}
