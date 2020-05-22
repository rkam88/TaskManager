package com.langfordapps.taskmanager.edit.presentation.dialogs

import com.langfordapps.taskmanager.commons.domain.model.DateType
import java.util.Calendar

interface OnDatePickerResultListener {

    fun onDateSet(dateType: DateType, newDate: Calendar)

}