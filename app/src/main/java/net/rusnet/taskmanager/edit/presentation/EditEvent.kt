package net.rusnet.taskmanager.edit.presentation

sealed class EditEvents {
    object SetTitleForNewTask : EditEvents()
    object SetTitleForExistingTask : EditEvents()
    object NavigateBack : EditEvents()
    object ShowExitConfirmationDialog : EditEvents()
}