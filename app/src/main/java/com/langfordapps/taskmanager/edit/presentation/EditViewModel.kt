package com.langfordapps.taskmanager.edit.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.langfordapps.taskmanager.R
import com.langfordapps.taskmanager.commons.domain.model.DateType
import com.langfordapps.taskmanager.commons.domain.model.DateType.ALARM_DATE
import com.langfordapps.taskmanager.commons.domain.model.DateType.END_DATE
import com.langfordapps.taskmanager.commons.domain.model.DateType.START_DATE
import com.langfordapps.taskmanager.commons.domain.model.Task
import com.langfordapps.taskmanager.commons.domain.model.TaskType
import com.langfordapps.taskmanager.commons.extensions.areDatesAllDay
import com.langfordapps.taskmanager.commons.extensions.exhaustive
import com.langfordapps.taskmanager.commons.extensions.getInitialTaskDate
import com.langfordapps.taskmanager.commons.extensions.getOrInitAlarmDate
import com.langfordapps.taskmanager.commons.extensions.getOrInitEndDate
import com.langfordapps.taskmanager.commons.extensions.getOrInitStartDate
import com.langfordapps.taskmanager.commons.extensions.hasDates
import com.langfordapps.taskmanager.commons.extensions.setDatesToAllDayAndCopy
import com.langfordapps.taskmanager.commons.presentation.DateFormatHelper
import com.langfordapps.taskmanager.commons.presentation.SingleLiveEvent
import com.langfordapps.taskmanager.commons.presentation.ViewVisibility.GONE
import com.langfordapps.taskmanager.commons.presentation.ViewVisibility.VISIBLE
import com.langfordapps.taskmanager.edit.domain.SaveTaskUseCase
import com.langfordapps.taskmanager.edit.presentation.EditEvents.NavigateBack
import com.langfordapps.taskmanager.edit.presentation.EditEvents.SetTaskNameCursorToEnd
import com.langfordapps.taskmanager.edit.presentation.EditEvents.ShowDatePickerDialog
import com.langfordapps.taskmanager.edit.presentation.EditEvents.ShowExitConfirmationDialog
import com.langfordapps.taskmanager.edit.presentation.EditEvents.ShowKeyboard
import com.langfordapps.taskmanager.edit.presentation.EditEvents.ShowTimePickerDialog
import com.langfordapps.taskmanager.edit.presentation.dialogs.OnDatePickerResultListener
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

private const val EMPTY_STRING = ""
private const val SHOW_KEYBOARD_DELAY_MS = 250L

class EditViewModel @Inject constructor(
    private val saveTaskUseCase: SaveTaskUseCase
) : ViewModel(),
    OnDatePickerResultListener {

    private lateinit var initialTask: Task
    private lateinit var currentTask: Task
    val event = SingleLiveEvent<EditEvents>()
    val editViewState = MutableLiveData<EditViewState>()

    fun initViewModelFromIntent(intentTask: Task?, intentTaskType: TaskType?, showDates: Boolean) {
        if (!this::initialTask.isInitialized) {
            initialTask = when {
                intentTask != null -> intentTask
                intentTaskType != null -> {
                    viewModelScope.launch {
                        delay(SHOW_KEYBOARD_DELAY_MS)
                        event.postValue(ShowKeyboard)
                    }
                    if (showDates) {
                        Task(
                            taskType = intentTaskType,
                            startDate = Task.getInitialTaskDate(),
                            endDate = Task.getInitialTaskDate()
                        )
                    } else {
                        Task(taskType = intentTaskType)
                    }
                }
                else -> throw IllegalArgumentException("EditViewModel: intentTask and taskType can't be both null")
            }
            updateCurrentState(initialTask.copy(), initialTask.areDatesAllDay())
            event.postValue(SetTaskNameCursorToEnd)
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
                startDate = Task.getInitialTaskDate(),
                endDate = Task.getInitialTaskDate()
            )
        )
    }

    fun onAllDaySwitchChange(isChecked: Boolean) {
        if (isChecked) {
            updateCurrentState(currentTask.setDatesToAllDayAndCopy(), true)
        } else {
            updateCurrentState(currentTask, false)
        }
    }

    fun onDeleteDatePressed() {
        updateCurrentState(currentTask.copy(startDate = null, endDate = null))
    }

    fun onStartDateClicked() {
        val initialDate = Calendar.getInstance().apply {
            timeInMillis = currentTask.getOrInitStartDate()
        }
        event.postValue(ShowDatePickerDialog(START_DATE, initialDate))
    }

    fun onEndDateClicker() {
        val initialDate = Calendar.getInstance().apply {
            timeInMillis = currentTask.getOrInitEndDate()
        }
        event.postValue(ShowDatePickerDialog(END_DATE, initialDate))
    }

    override fun onDateSet(dateType: DateType, newDate: Calendar) {
        when (dateType) {
            START_DATE -> {
                val newStartDate = newDate.timeInMillis
                when {
                    editViewState.value?.isAllDay == true -> {
                        updateCurrentState(currentTask.copy(startDate = newStartDate).setDatesToAllDayAndCopy())
                    }
                    newStartDate > currentTask.getOrInitEndDate() -> {
                        updateCurrentState(currentTask.copy(startDate = newStartDate, endDate = newStartDate))
                    }
                    else -> {
                        updateCurrentState(currentTask.copy(startDate = newStartDate))
                    }
                }
            }
            END_DATE -> {
                val newEndDate = newDate.timeInMillis
                if (newEndDate < currentTask.getOrInitStartDate()) {
                    updateCurrentState(currentTask.copy(startDate = newEndDate, endDate = newEndDate))
                } else {
                    updateCurrentState(currentTask.copy(endDate = newEndDate))
                }
            }
            ALARM_DATE -> updateCurrentState(currentTask.copy(alarmDate = newDate.timeInMillis))
        }.exhaustive
    }

    fun onStartTimeClicked() {
        val initialDate = Calendar.getInstance().apply {
            timeInMillis = currentTask.getOrInitStartDate()
        }
        event.postValue(ShowTimePickerDialog(START_DATE, initialDate))
    }

    fun onEndTimeClicker() {
        val initialDate = Calendar.getInstance().apply {
            timeInMillis = currentTask.getOrInitEndDate()
        }
        event.postValue(ShowTimePickerDialog(END_DATE, initialDate))
    }

    fun onSaveClicked() {
        if (currentTask.name == EMPTY_STRING) {
            event.postValue(EditEvents.ShowNoTaskNameMessage)
        } else {
            viewModelScope.launch {
                saveTaskUseCase.execute(currentTask)
                event.postValue(EditEvents.FinishActivityWithPositiveResult)
            }
        }
    }

    fun onAddAlarmPressed() {
        updateCurrentState(currentTask.copy(alarmDate = currentTask.getOrInitAlarmDate()))
    }

    fun onDeleteAlarmPressed() {
        updateCurrentState(currentTask.copy(alarmDate = null))
    }

    fun onAlarmDatePressed() {
        val initialDate = Calendar.getInstance().apply {
            timeInMillis = currentTask.getOrInitAlarmDate()
        }
        event.postValue(ShowDatePickerDialog(ALARM_DATE, initialDate))
    }

    fun onAlarmTimePressed() {
        val initialDate = Calendar.getInstance().apply {
            timeInMillis = currentTask.getOrInitAlarmDate()
        }
        event.postValue(ShowTimePickerDialog(ALARM_DATE, initialDate))
    }

    private fun updateCurrentState(updatedTask: Task, isAllDay: Boolean = editViewState.value?.isAllDay ?: false) {
        currentTask = updatedTask
        val newState = EditViewState(
            toolbarTitleStringResId = if (initialTask.id == 0L) R.string.title_new_task else R.string.title_existing_task,
            taskName = currentTask.name,
            taskType = currentTask.taskType,
            addDateButtonVisibility = if (currentTask.hasDates()) GONE else VISIBLE,
            dateLayoutVisibility = if (currentTask.hasDates()) VISIBLE else GONE,
            startDate = DateFormatHelper.formatDate(currentTask.getOrInitStartDate()),
            isAllDay = isAllDay,
            additionalDatePickersVisibility = if (isAllDay) GONE else VISIBLE,
            startTime = DateFormatHelper.formatTime(currentTask.getOrInitStartDate()),
            endDate = DateFormatHelper.formatDate(currentTask.getOrInitEndDate()),
            endTime = DateFormatHelper.formatTime(currentTask.getOrInitEndDate()),
            addAlarmButtonVisibility = if (currentTask.alarmDate == null) VISIBLE else GONE,
            alarmLayoutVisibility = if (currentTask.alarmDate == null) GONE else VISIBLE,
            alarmDate = DateFormatHelper.formatDate(currentTask.alarmDate ?: 0),
            alarmTime = DateFormatHelper.formatTime(currentTask.alarmDate ?: 0)
        )
        editViewState.postValue(newState)
    }

}
