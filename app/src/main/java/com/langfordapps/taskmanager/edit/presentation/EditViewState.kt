package com.langfordapps.taskmanager.edit.presentation

import androidx.annotation.StringRes
import com.langfordapps.taskmanager.commons.domain.model.TaskType
import com.langfordapps.taskmanager.commons.presentation.ViewVisibility

data class EditViewState(
    @StringRes val toolbarTitleStringResId: Int,
    val taskName: String,
    val taskType: TaskType,
    val addDateButtonVisibility: ViewVisibility,
    val dateLayoutVisibility: ViewVisibility,
    val startDate: String,
    val isAllDay: Boolean,
    val additionalDatePickersVisibility: ViewVisibility,
    val startTime: String,
    val endDate: String,
    val endTime: String,
    val addAlarmButtonVisibility: ViewVisibility,
    val alarmLayoutVisibility: ViewVisibility,
    val alarmDate: String,
    val alarmTime: String
)