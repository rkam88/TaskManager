package net.rusnet.taskmanager.tasksdisplay.presentation

import androidx.annotation.IdRes
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TasksDisplayViewModel : ViewModel() {

    val currentTasksDisplayState = MutableLiveData(TasksDisplayState.INBOX)

    fun onDrawerItemSelected(@IdRes menuItemId: Int) {
        currentTasksDisplayState.postValue(menuItemId.getTasksDisplayState())
    }

    fun onAddButtonClicked() {
        // todo: impl this
    }


}