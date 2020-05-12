package net.rusnet.taskmanager.edit.presentation

import androidx.lifecycle.ViewModel
import net.rusnet.taskmanager.commons.SingleLiveEvent
import net.rusnet.taskmanager.commons.domain.model.TaskType
import net.rusnet.taskmanager.edit.presentation.EditEvents.NavigateBack
import net.rusnet.taskmanager.edit.presentation.EditEvents.ShowExitConfirmationDialog
import net.rusnet.taskmanager_old.commons.domain.model.Task
import javax.inject.Inject

class EditViewModel @Inject constructor() : ViewModel() {

    private var initialTask: Task? = null
    private var currentTask: Task? = null
    val event = SingleLiveEvent<EditEvents>()

    fun initViewModelFromIntent(intentTask: Task?, intentTaskType: TaskType?) {
        if (initialTask == null) {
            initialTask = when {
                intentTask != null -> intentTask
                intentTaskType != null -> Task(type = intentTaskType)
                else -> throw IllegalArgumentException("intentTask and taskType can't be both null")
            }
            currentTask = initialTask!!.copy()
        }
        if (initialTask!!.id == 0L) {
            event.postValue(EditEvents.SetTitleForNewTask)
        } else {
            event.postValue(EditEvents.SetTitleForExistingTask)
        }
    }

    fun onBackPressed() {
        if (currentTask == initialTask) {
            event.postValue(NavigateBack)
        } else {
            event.postValue(ShowExitConfirmationDialog)
        }
    }

}