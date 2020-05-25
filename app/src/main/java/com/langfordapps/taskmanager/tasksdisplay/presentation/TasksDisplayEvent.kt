package com.langfordapps.taskmanager.tasksdisplay.presentation

sealed class TasksDisplayEvent {
    data class ShowConfirmationDialog(val dialogTitle: String, val dialogTag: String) : TasksDisplayEvent()
    object FinishActionMode : TasksDisplayEvent()
    object ShowNoTasksToDeleteMessage : TasksDisplayEvent()
}