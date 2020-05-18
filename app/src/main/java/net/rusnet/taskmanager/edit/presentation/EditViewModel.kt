package net.rusnet.taskmanager.edit.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import net.rusnet.taskmanager.R
import net.rusnet.taskmanager.commons.domain.model.DateType
import net.rusnet.taskmanager.commons.domain.model.DateType.END_DATE
import net.rusnet.taskmanager.commons.domain.model.DateType.START_DATE
import net.rusnet.taskmanager.commons.domain.model.Task
import net.rusnet.taskmanager.commons.domain.model.TaskType
import net.rusnet.taskmanager.commons.extensions.exhaustive
import net.rusnet.taskmanager.commons.presentation.SingleLiveEvent
import net.rusnet.taskmanager.edit.presentation.EditEvents.NavigateBack
import net.rusnet.taskmanager.edit.presentation.EditEvents.ShowDatePickerDialog
import net.rusnet.taskmanager.edit.presentation.EditEvents.ShowExitConfirmationDialog
import net.rusnet.taskmanager.edit.presentation.EditEvents.ShowTimePickerDialog
import net.rusnet.taskmanager.edit.presentation.dialogs.OnDatePickerResultListener
import java.util.Calendar
import javax.inject.Inject

class EditViewModel @Inject constructor() : ViewModel(),
                                            OnDatePickerResultListener {

    private lateinit var initialTask: Task
    lateinit var currentTask: Task
        private set
    val event = SingleLiveEvent<EditEvents>()
    val editViewState = MutableLiveData<EditViewState>()

    fun initViewModelFromIntent(intentTask: Task?, intentTaskType: TaskType?) {
        if (!this::initialTask.isInitialized) {
            initialTask = when {
                intentTask != null -> intentTask
                intentTaskType != null -> Task(taskType = intentTaskType)
                else -> throw IllegalArgumentException("EditViewModel: intentTask and taskType can't be both null")
            }
            currentTask = initialTask.copy()
        }
    }

    fun onBackPressed() {
        if (currentTask == initialTask) {
            event.postValue(NavigateBack)
        } else {
            event.postValue(ShowExitConfirmationDialog)
        }
    }

    fun onTaskNameChanged(newName: String) {
        updateCurrentState(currentTask.copy(name = newName))
    }

    fun onTasksTypeSelected(position: Int) {
        val newType = TaskType.values().find { it.spinnerPosition == position }!!
        updateCurrentState(currentTask.copy(taskType = newType))
    }

    fun onAddDatePressed() {
        updateCurrentState(
            currentTask.copy(
                startDate = getInitialStartDate(),
                endDate = getInitialEndDate()
            )
        )
    }

    fun onDeleteDatePressed() {
        updateCurrentState(currentTask.copy(startDate = null, endDate = null))
    }

    fun onStartDateClicked() {
        val initialDate = Calendar.getInstance().apply {
            timeInMillis = currentTask.startDate ?: getInitialStartDate()
        }
        event.postValue(ShowDatePickerDialog(START_DATE, initialDate))
    }

    fun onEndDateClicker() {
        val initialDate = Calendar.getInstance().apply {
            timeInMillis = currentTask.endDate ?: getInitialEndDate()
        }
        event.postValue(ShowDatePickerDialog(END_DATE, initialDate))
    }

    override fun onDateSet(dateType: DateType, newDate: Calendar) {
        when (dateType) {
            START_DATE -> {
                val newStartDate = newDate.timeInMillis
                if (newStartDate > currentTask.endDate ?: getInitialEndDate()) {
                    updateCurrentState(currentTask.copy(startDate = newStartDate, endDate = newStartDate))
                } else {
                    updateCurrentState(currentTask.copy(startDate = newStartDate))
                }
            }
            END_DATE -> {
                val newEndDate = newDate.timeInMillis
                if (newEndDate < currentTask.startDate ?: getInitialStartDate()) {
                    updateCurrentState(currentTask.copy(startDate = newEndDate, endDate = newEndDate))
                } else {
                    updateCurrentState(currentTask.copy(endDate = newEndDate))
                }
            }
        }.exhaustive
    }

    fun onStartTimeClicked() {
        val initialDate = Calendar.getInstance().apply {
            timeInMillis = currentTask.startDate ?: getInitialStartDate()
        }
        event.postValue(ShowTimePickerDialog(START_DATE, initialDate))
    }

    fun onEndTimeClicker() {
        val initialDate = Calendar.getInstance().apply {
            timeInMillis = currentTask.endDate ?: getInitialEndDate()
        }
        event.postValue(ShowTimePickerDialog(END_DATE, initialDate))
    }

    private fun updateCurrentState(updatedTask: Task) {
        currentTask = updatedTask
        val newState = EditViewState(
            toolbarTitleStringResId = if (initialTask.id == 0L) R.string.title_new_task else R.string.title_existing_task,
            taskName = currentTask.name,
            taskType = currentTask.taskType,
            showDates = currentTask.startDate != null,
            startDate = currentTask.startDate ?: 0,
            endDate = currentTask.endDate ?: 0
        )
        editViewState.postValue(newState)
    }

    private fun getInitialStartDate() = System.currentTimeMillis()

    private fun getInitialEndDate() = System.currentTimeMillis()

}
