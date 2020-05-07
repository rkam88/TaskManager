package net.rusnet.taskmanager.tasksdisplay.presentation

import androidx.annotation.IdRes
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import net.rusnet.taskmanager.commons.app.Router
import javax.inject.Inject

class TasksDisplayViewModel @Inject constructor(private val router: Router) : ViewModel() {

    val currentTasksDisplayState = MutableLiveData(TasksDisplayState.INBOX)

    fun onDrawerItemSelected(@IdRes menuItemId: Int) {
        currentTasksDisplayState.postValue(menuItemId.getTasksDisplayState())
    }

    fun onAddButtonClicked() {
        router.navigateToEdit()
    }

}