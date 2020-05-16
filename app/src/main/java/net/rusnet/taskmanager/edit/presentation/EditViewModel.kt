package net.rusnet.taskmanager.edit.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import net.rusnet.taskmanager.R
import net.rusnet.taskmanager.commons.domain.model.Task
import net.rusnet.taskmanager.commons.domain.model.TaskType
import net.rusnet.taskmanager.commons.presentation.SingleLiveEvent
import net.rusnet.taskmanager.edit.presentation.EditEvents.NavigateBack
import net.rusnet.taskmanager.edit.presentation.EditEvents.ShowExitConfirmationDialog
import javax.inject.Inject

class EditViewModel @Inject constructor() : ViewModel() {

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
                startDate = System.currentTimeMillis(),
                endDate = System.currentTimeMillis()
            )
        )
    }

    fun onDeleteDatePressed() {
        updateCurrentState(currentTask.copy(startDate = null, endDate = null))
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

}
