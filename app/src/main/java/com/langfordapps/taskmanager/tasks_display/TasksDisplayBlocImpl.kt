package com.langfordapps.taskmanager.tasks_display

import com.langfordapps.taskmanager.common.data.TasksRepository
import com.langfordapps.taskmanager.core.bloc.BaseBlocImpl
import com.langfordapps.taskmanager.core.extensions.exhaustive
import com.langfordapps.taskmanager.tasks_display.TasksDisplayAction.ShowDeleteConfirmationDialog
import com.langfordapps.taskmanager.tasks_display.TasksDisplayEvent.*

class TasksDisplayBlocImpl(
    initialState: TasksDisplayState = TasksDisplayState(taskList = emptyList()),
    override val parent: TasksDisplayParent,
    private val tasksRepository: TasksRepository,
) : TasksDisplayBloc,
    BaseBlocImpl<TasksDisplayState, TasksDisplayEvent, TasksDisplayAction, TasksDisplayParent>(
        initialState
    ) {

    init {
        updateTasksList()
    }

    override fun onNewEvent(event: TasksDisplayEvent) {
        when (event) {
            OnAddNewTaskClicked -> parent.navigateToTaskCreate()
            is OnDeleteClicked -> sendAction(ShowDeleteConfirmationDialog(event.task))
            is OnDeleteConfirmed -> {
                tasksRepository.deleteTask(event.task)
                updateTasksList()
            }
            is OnEditClicked -> parent.navigateToTaskEdit(event.task)
            is OnMarkedAsComplete -> {
                tasksRepository.markTaskAsComplete(event.task)
                updateTasksList()
            }
        }.exhaustive
    }

    private fun updateTasksList() {
        val newTasksList = tasksRepository.getAllTasks()
        updateState {
            it.copy(taskList = newTasksList)
        }
    }

}
