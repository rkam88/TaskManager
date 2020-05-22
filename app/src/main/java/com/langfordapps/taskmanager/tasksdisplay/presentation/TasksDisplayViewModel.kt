package com.langfordapps.taskmanager.tasksdisplay.presentation

import android.content.Context
import android.text.format.DateFormat
import android.view.View
import androidx.annotation.IdRes
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import com.langfordapps.taskmanager.R
import com.langfordapps.taskmanager.commons.app.Router
import com.langfordapps.taskmanager.commons.extensions.areDatesAllDay
import com.langfordapps.taskmanager.commons.extensions.doStartAndEndDatesMatch
import com.langfordapps.taskmanager.commons.extensions.doStartAndEndDaysMatch
import com.langfordapps.taskmanager.commons.extensions.doTimesMatchDayStart
import com.langfordapps.taskmanager.commons.extensions.hasDates
import com.langfordapps.taskmanager.commons.extensions.isOverdue
import com.langfordapps.taskmanager.commons.presentation.ConfirmationDialogFragment.ConfirmationDialogListener
import com.langfordapps.taskmanager.commons.presentation.SingleLiveEvent
import com.langfordapps.taskmanager.tasksdisplay.domain.DeleteCompletedTasksUseCase
import com.langfordapps.taskmanager.tasksdisplay.domain.DeleteTasksUseCase
import com.langfordapps.taskmanager.tasksdisplay.domain.GetTaskByIdUseCase
import com.langfordapps.taskmanager.tasksdisplay.domain.GetTasksCountUseCase
import com.langfordapps.taskmanager.tasksdisplay.domain.GetTasksUseCase
import com.langfordapps.taskmanager.tasksdisplay.domain.MarkTaskAsCompletedUseCase
import com.langfordapps.taskmanager.tasksdisplay.presentation.TasksDisplayEvent.FinishActionMode
import com.langfordapps.taskmanager.tasksdisplay.presentation.TasksDisplayEvent.ShowConfirmationDialog
import com.langfordapps.taskmanager.tasksdisplay.presentation.model.ViewTask
import javax.inject.Inject

private const val COUNT_99_PLUS = "99+"
private const val TAG_DELETE_COMPLETED_TASKS = "TAG_DELETE_COMPLETED_TASKS"
private const val TAG_DELETE_SELECTED_TASKS = "TAG_DELETE_ACTION_MODE"
private const val ZERO = 0
private const val TASK_COUNT_MAX_VALUE = 99

class TasksDisplayViewModel @Inject constructor(
    private val applicationContext: Context,
    private val router: Router,
    private val getTasksUseCase: GetTasksUseCase,
    private val getTasksCountUseCase: GetTasksCountUseCase,
    private val getTaskByIdUseCase: GetTaskByIdUseCase,
    private val markTaskAsCompletedUseCase: MarkTaskAsCompletedUseCase,
    private val deleteCompletedTasksUseCase: DeleteCompletedTasksUseCase,
    private val deleteTasksUseCase: DeleteTasksUseCase
) : ViewModel(),
    ConfirmationDialogListener {

    val event = SingleLiveEvent<TasksDisplayEvent>()
    val currentTasksDisplayState = MutableLiveData<TasksDisplayState>()
    val currentViewTasks = MutableLiveData<List<ViewTask>>()
    val currentTaskCount = MutableLiveData<Map<@androidx.annotation.IdRes Int, String>>()

    init {
        onDrawerItemSelected(TasksDisplayState.Inbox.navigationViewMenuId)
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
        val isCalendar = currentState == TasksDisplayState.Calendar
        router.navigateToEdit(
            currentState.newTaskType,
            isCalendar,
            TasksDisplayActivity.REQUEST_CODE_SAVE_TASK
        )
    }

    fun onPositiveResultFromEditActivity() = syncViewModelWithDb()

    fun onTaskClick(taskId: Long) {
        if (currentTasksDisplayState.value!!.isActionModeEnabled) {
            onTaskLongClick(taskId)
        } else {
            viewModelScope.launch {
                val task = getTaskByIdUseCase.execute(taskId)
                router.navigateToEdit(
                    task,
                    TasksDisplayActivity.REQUEST_CODE_SAVE_TASK
                )
            }
        }
    }

    fun onTaskLongClick(taskId: Long) {
        val viewTasks = currentViewTasks.value!!
        val clickedTask = viewTasks.find { task -> task.taskId == taskId }
        if (clickedTask != null) {
            clickedTask.isSelectedForDeletion = !clickedTask.isSelectedForDeletion
        }
        val tasksMarkedForDeletion = viewTasks.count { it.isSelectedForDeletion }
        if (tasksMarkedForDeletion == ZERO) {
            event.postValue(FinishActionMode)
        } else {
            val newTitle = applicationContext.resources.getQuantityString(
                R.plurals.n_tasks_selected,
                tasksMarkedForDeletion,
                tasksMarkedForDeletion
            )
            val newState = TasksDisplayState.Custom(
                currentTasksDisplayState.value!!,
                newIsSwipeEnabled = false,
                newIsActionModeEnabled = true,
                newActionModeTitle = newTitle
            )
            currentTasksDisplayState.postValue(newState)
        }
        currentViewTasks.postValue(viewTasks)
    }

    fun onTaskSwipedLeft(taskId: Long) {
        viewModelScope.launch {
            markTaskAsCompletedUseCase.execute(taskId)
            syncViewModelWithDb()
        }
    }

    fun onDeleteCompletedTasksClicked() {
        if (currentTasksDisplayState.value == TasksDisplayState.Completed) {
            val tag = TAG_DELETE_COMPLETED_TASKS
            val title = applicationContext.getString(R.string.delete_completed_dialog_title)
            event.postValue(
                ShowConfirmationDialog(
                    dialogTag = tag,
                    dialogTitle = title
                )
            )
        }
    }

    fun onDestroyActionMode() {
        val currentTasks = currentViewTasks.value!!
        for (task in currentTasks) {
            task.isSelectedForDeletion = false
        }
        currentViewTasks.postValue(currentTasks)

        val newState = TasksDisplayState.Custom(
            currentTasksDisplayState.value!!,
            newIsSwipeEnabled = true,
            newIsActionModeEnabled = false
        )
        currentTasksDisplayState.postValue(newState)
    }

    fun onDeleteClicked() {
        val tag = TAG_DELETE_SELECTED_TASKS
        val tasksMarkedForDeletion = currentViewTasks.value!!.count { it.isSelectedForDeletion }
        val title = applicationContext.resources.getQuantityString(
            R.plurals.delete_tasks_dialog_title,
            tasksMarkedForDeletion,
            tasksMarkedForDeletion
        )
        event.postValue(
            ShowConfirmationDialog(
                dialogTag = tag,
                dialogTitle = title
            )
        )
    }

    override fun onPositiveResponse(dialogTag: String) {
        when (dialogTag) {
            TAG_DELETE_COMPLETED_TASKS -> {
                viewModelScope.launch {
                    deleteCompletedTasksUseCase.execute()
                    syncViewModelWithDb()
                }
            }
            TAG_DELETE_SELECTED_TASKS -> {
                val tasksIdsToDelete = currentViewTasks.value!!
                    .filter { it.isSelectedForDeletion }
                    .map { it.taskId }
                viewModelScope.launch {
                    deleteTasksUseCase.execute(tasksIdsToDelete)
                    syncViewModelWithDb()
                    event.postValue(FinishActionMode)
                }
            }
        }
    }

    override fun onNegativeResponse(dialogTag: String) {
        when (dialogTag) {
            TAG_DELETE_COMPLETED_TASKS -> {
            }
            TAG_DELETE_SELECTED_TASKS -> event.postValue(FinishActionMode)
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
            val tasksCount = BASE_TASK_DISPLAY_STATES.map { state ->
                val count = getTasksCountUseCase.execute(state.baseFilter)
                val countAsString = if (count <= TASK_COUNT_MAX_VALUE) count.toString() else COUNT_99_PLUS
                return@map state.navigationViewMenuId to countAsString
            }.toMap()
            currentTaskCount.postValue(tasksCount)
        }
    }

}