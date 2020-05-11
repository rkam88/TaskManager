package net.rusnet.taskmanager.tasksdisplay.presentation

import androidx.annotation.IdRes
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import net.rusnet.taskmanager.commons.app.Router
import net.rusnet.taskmanager.tasksdisplay.domain.GetTasksUseCase
import net.rusnet.taskmanager_old.commons.domain.model.Task
import javax.inject.Inject

class TasksDisplayViewModel @Inject constructor(
    private val router: Router,
    private val getTasksUseCase: GetTasksUseCase
) : ViewModel() {

    val currentTasksDisplayState = MutableLiveData<TasksDisplayState>()
    val currentTasks = MutableLiveData<List<Task>>()

    init {
        onDrawerItemSelected(TasksDisplayState.INBOX.navigationViewMenuId)
    }

    fun onDrawerItemSelected(@IdRes menuItemId: Int) {
        menuItemId.getTasksDisplayState().also { newState ->
            currentTasksDisplayState.postValue(newState)
            updateCurrentTasks(newState)
        }
    }

    fun onAddButtonClicked() {
        router.navigateToEdit()
    }

    private fun updateCurrentTasks(state: TasksDisplayState) {
        viewModelScope.launch {
            currentTasks.postValue(getTasksUseCase.execute(state.baseFilter))
        }
    }

}