package net.rusnet.taskmanager.tasksdisplay.presentation

sealed class TasksDisplayEvent {
    data class ShowConfirmationDialog(val dialogTitle: String, val dialogTag: String) : TasksDisplayEvent()
}