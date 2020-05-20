package net.rusnet.taskmanager.tasksdisplay.presentation

import android.content.Context
import android.text.format.DateFormat
import android.view.View
import androidx.annotation.IdRes
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import net.rusnet.taskmanager.R
import net.rusnet.taskmanager.commons.app.Router
import net.rusnet.taskmanager.commons.extensions.areDatesAllDay
import net.rusnet.taskmanager.commons.extensions.doStartAndEndDatesMatch
import net.rusnet.taskmanager.commons.extensions.doStartAndEndDaysMatch
import net.rusnet.taskmanager.commons.extensions.doTimesMatchDayStart
import net.rusnet.taskmanager.commons.extensions.hasDates
import net.rusnet.taskmanager.commons.extensions.isOverdue
import net.rusnet.taskmanager.tasksdisplay.domain.GetTaskByIdUseCase
import net.rusnet.taskmanager.tasksdisplay.domain.GetTasksCountUseCase
import net.rusnet.taskmanager.tasksdisplay.domain.GetTasksUseCase
import net.rusnet.taskmanager.tasksdisplay.domain.MarkTaskAsCompletedUseCase
import net.rusnet.taskmanager.tasksdisplay.presentation.model.ViewTask
import javax.inject.Inject

private const val COUNT_99_PLUS = "99+"

class TasksDisplayViewModel @Inject constructor(
    private val applicationContext: Context,
    private val router: Router,
    private val getTasksUseCase: GetTasksUseCase,
    private val getTasksCountUseCase: GetTasksCountUseCase,
    private val getTaskByIdUseCase: GetTaskByIdUseCase,
    private val markTaskAsCompletedUseCase: MarkTaskAsCompletedUseCase
) : ViewModel() {

    val currentTasksDisplayState = MutableLiveData<TasksDisplayState>()
    val currentViewTasks = MutableLiveData<List<ViewTask>>()
    val currentTaskCount = MutableLiveData<Map<@androidx.annotation.IdRes Int, String>>()

    init {
        onDrawerItemSelected(TasksDisplayState.INBOX.navigationViewMenuId)
        updateTasksCount()
    }

    fun onDrawerItemSelected(@IdRes menuItemId: Int) {
        menuItemId.getTasksDisplayState().also { newState ->
            currentTasksDisplayState.postValue(newState)
            updateCurrentTasks(newState)
        }
    }

    fun onAddButtonClicked() {
        val currentState = requireNotNull(currentTasksDisplayState.value)
        val isCalendar = currentState == TasksDisplayState.CALENDAR
        router.navigateToEdit(
            currentState.newTaskType,
            isCalendar,
            TasksDisplayActivity.REQUEST_CODE_SAVE_TASK
        )
    }

    fun onPositiveResultFromEditActivity() = syncViewModelWithDb()

    fun onTaskClick(taskId: Long) {
        viewModelScope.launch {
            val task = getTaskByIdUseCase.execute(taskId)
            router.navigateToEdit(
                task,
                TasksDisplayActivity.REQUEST_CODE_SAVE_TASK
            )
        }
    }

    fun onTaskLongClick(taskId: Long) {
        // todo handle item long click
    }

    fun onTaskSwipedLeft(taskId: Long) {
        viewModelScope.launch {
            markTaskAsCompletedUseCase.execute(taskId)
            syncViewModelWithDb()
        }
    }

    private fun syncViewModelWithDb() {
        currentTasksDisplayState.value?.let {
            updateCurrentTasks(it)
            updateTasksCount()
        }
    }

    private fun updateCurrentTasks(state: TasksDisplayState) {
        viewModelScope.launch {
            val viewTasksList = getTasksUseCase.execute(state.baseFilter)
                .map {
                    val datesAsString = if (it.hasDates()) {
                        val startDay = DateFormat.getDateFormat(applicationContext).format(it.startDate)
                        val startTime = DateFormat.getTimeFormat(applicationContext).format(it.startDate)
                        val endDay = DateFormat.getDateFormat(applicationContext).format(it.endDate)
                        val endTime = DateFormat.getTimeFormat(applicationContext).format(it.endDate)
                        when {
                            it.doStartAndEndDatesMatch() -> "$startDay, $startTime"
                            it.areDatesAllDay() -> startDay
                            it.doStartAndEndDaysMatch() -> "$startDay, $startTime - $endTime"
                            it.doTimesMatchDayStart() -> "$startDay - $endDay"
                            else -> "$startDay, $startTime - $endDay, $endTime"
                        }
                    } else {
                        null
                    }
                    return@map ViewTask(
                        taskId = it.id,
                        name = it.name,
                        dateViewVisibility = if (it.hasDates()) View.VISIBLE else View.GONE,
                        date = datesAsString,
                        dateColor = if (it.isOverdue()) R.color.itemOverdue else R.color.colorTextPrimary
                    )
                }
            currentViewTasks.postValue(viewTasksList)
        }
    }

    private fun updateTasksCount() {
        viewModelScope.launch {
            val tasksCount = TasksDisplayState.values().map { state ->
                val count = getTasksCountUseCase.execute(state.baseFilter)
                val countAsString = if (count < 100) count.toString() else COUNT_99_PLUS
                return@map state.navigationViewMenuId to countAsString
            }.toMap()
            currentTaskCount.postValue(tasksCount)
        }
    }

}