package com.langfordapps.taskmanager.edit.presentation

import com.langfordapps.taskmanager.commons.domain.model.DateType
import java.util.Calendar

sealed class EditEvents {
    object ShowKeyboard : EditEvents()
    object NavigateBack : EditEvents()
    object ShowExitConfirmationDialog : EditEvents()
    data class ShowDatePickerDialog(val dateType: DateType, val initialDialogDate: Calendar) : EditEvents()
    data class ShowTimePickerDialog(val dateType: DateType, val initialDialogDate: Calendar) : EditEvents()
    object ShowNoTaskNameMessage : EditEvents()
    object FinishActivityWithPositiveResult : EditEvents()
}