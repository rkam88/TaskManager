package net.rusnet.taskmanager.edit.presentation.dialogs

import net.rusnet.taskmanager.commons.domain.model.DateType
import java.util.Calendar

interface OnDatePickerResultListener {

    fun onDateSet(dateType: DateType, newDate: Calendar)

}