package net.rusnet.taskmanager.edit.presentation

sealed class EditEvents {
    object NavigateBack : EditEvents()
    object ShowExitConfirmationDialog : EditEvents()
}