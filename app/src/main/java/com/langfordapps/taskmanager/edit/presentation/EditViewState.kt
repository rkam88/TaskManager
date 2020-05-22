package com.langfordapps.taskmanager.edit.presentation

import android.view.View
import androidx.annotation.StringRes
import com.langfordapps.taskmanager.commons.domain.model.TaskType

data class EditViewState(
    @StringRes val toolbarTitleStringResId: Int,
    val taskName: String,
    val taskType: TaskType,
    val addDateButtonVisibility: Int,
    val dateLayoutVisibility: Int,
    val startDate: String,
    val isAllDay: Boolean,
    val additionalDatePickersVisibility: Int,
    val startTime: String,
    val endDate: String,
    val endTime: String,
    val addAlarmButtonVisibility: Int,
    val alarmLayoutVisibility: Int,
    val alarmDate: String,
    val alarmTime: String
) {
    init {
        fun validateVisibilityValue(value: Int, parameterName: String) {
            if (!(value == View.GONE || value == View.VISIBLE)) throw IllegalArgumentException("$parameterName should be either View.GONE or View.Visible")
        }

        validateVisibilityValue(addDateButtonVisibility, "addDateButtonVisibility")
        validateVisibilityValue(dateLayoutVisibility,"dateLayoutVisibility")
        validateVisibilityValue(additionalDatePickersVisibility,"additionalDatePickersVisibility")
        validateVisibilityValue(addAlarmButtonVisibility,"addAlarmButtonVisibility")
        validateVisibilityValue(alarmLayoutVisibility,"alarmLayoutVisibility")
    }
}