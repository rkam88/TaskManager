package net.rusnet.taskmanager.edit.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import net.rusnet.taskmanager.commons.domain.model.TaskType
import net.rusnet.taskmanager_old.commons.domain.model.Task
import javax.inject.Inject

class EditViewModel @Inject constructor() : ViewModel() {

    private var initialTask: Task? = null
    private var currentTask: Task? = null

    fun initViewModelFromIntent(intentTask: Task?, intentTaskType: TaskType?) {
        if (initialTask == null) {
            Log.d("DEBUG_TAG", "initial task set!")
            initialTask = when {
                intentTask != null -> intentTask
                intentTaskType != null -> Task(type = intentTaskType)
                else -> throw IllegalArgumentException("intentTask and taskType can't be both null")
            }
            currentTask = initialTask!!.copy()
        }
    }

}