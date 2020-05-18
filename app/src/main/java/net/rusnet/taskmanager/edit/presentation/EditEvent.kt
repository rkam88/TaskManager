package net.rusnet.taskmanager.edit.presentation

import net.rusnet.taskmanager.commons.domain.model.DateType
import java.util.Calendar

sealed class EditEvents {
    object NavigateBack : EditEvents()
    object ShowExitConfirmationDialog : EditEvents()
    data class ShowDatePickerDialog(val dateType: DateType, val initialDialogDate: Calendar) : EditEvents()
}