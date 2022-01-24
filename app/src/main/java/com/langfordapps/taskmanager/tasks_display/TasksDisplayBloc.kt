package com.langfordapps.taskmanager.tasks_display

import com.langfordapps.taskmanager.common.domain.Task
import com.langfordapps.taskmanager.core.bloc.*

interface TasksDisplayBloc
    : BaseBloc<TasksDisplayState,
        TasksDisplayEvent,
        TasksDisplayAction,
        TasksDisplayParent>

data class TasksDisplayState(
    val taskList: List<Task>
) : BaseState

sealed interface TasksDisplayEvent : BaseEvent {
    class OnMarkedAsComplete(val task: Task) : TasksDisplayEvent
    class OnDeleteClicked(val task: Task) : TasksDisplayEvent
    class OnEditClicked(val task: Task) : TasksDisplayEvent
    object OnAddNewTaskClicked : TasksDisplayEvent
    class OnDeleteConfirmed(val task: Task) : TasksDisplayEvent
}

sealed interface TasksDisplayAction : BaseAction {
    class ShowDeleteConfirmationDialog(val task: Task) : TasksDisplayAction
}

interface TasksDisplayParent : BaseBlocParent {
    fun navigateToTaskCreate()
    fun navigateToTaskEdit(task: Task)
}