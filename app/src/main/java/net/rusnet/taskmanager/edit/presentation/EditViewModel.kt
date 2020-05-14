package net.rusnet.taskmanager.edit.presentation

import androidx.lifecycle.ViewModel
import net.rusnet.taskmanager.R
import net.rusnet.taskmanager.commons.SingleLiveEvent
import net.rusnet.taskmanager.commons.domain.model.TaskType
import net.rusnet.taskmanager.edit.presentation.EditEvents.NavigateBack
import net.rusnet.taskmanager.edit.presentation.EditEvents.ShowExitConfirmationDialog
import net.rusnet.taskmanager.commons.domain.model.Task
import javax.inject.Inject

class EditViewModel @Inject constructor() : ViewModel() {

    private lateinit var initialTask: Task
    lateinit var currentTask: Task
        private set
    val event = SingleLiveEvent<EditEvents>()

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

    fun getToolbarTitleStringResId() = if (initialTask.id == 0L) R.string.title_new_task else R.string.title_existing_task

    fun onTaskNameChanged(newName: String) {
        currentTask = currentTask.copy(name = newName)
    }

    fun onTasksTypeSelected(position: Int) {
        val newType = TaskType.values().find { it.spinnerPosition == position }!!
        currentTask = currentTask.copy(taskType = newType)
    }

}