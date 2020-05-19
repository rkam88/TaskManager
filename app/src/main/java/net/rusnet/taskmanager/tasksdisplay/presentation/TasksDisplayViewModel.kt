package net.rusnet.taskmanager.tasksdisplay.presentation

import androidx.annotation.IdRes
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import net.rusnet.taskmanager.commons.app.Router
import net.rusnet.taskmanager.commons.domain.model.Task
import net.rusnet.taskmanager.tasksdisplay.domain.GetTasksCountUseCase
import net.rusnet.taskmanager.tasksdisplay.domain.GetTasksUseCase
import javax.inject.Inject

private const val COUNT_99_PLUS = "99+"

class TasksDisplayViewModel @Inject constructor(
    private val router: Router,
    private val getTasksUseCase: GetTasksUseCase,
    private val getTasksCountUseCase: GetTasksCountUseCase
) : ViewModel() {

    val currentTasksDisplayState = MutableLiveData<TasksDisplayState>()
    val currentTasks = MutableLiveData<List<Task>>()
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

    fun onPositiveResultFromEditActivity() {
        currentTasksDisplayState.value?.let {
            updateCurrentTasks(it)
            updateTasksCount()
        }
    }

    private fun updateCurrentTasks(state: TasksDisplayState) {
        viewModelScope.launch {
            currentTasks.postValue(getTasksUseCase.execute(state.baseFilter))
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