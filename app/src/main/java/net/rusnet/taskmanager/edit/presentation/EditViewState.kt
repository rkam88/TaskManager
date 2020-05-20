package net.rusnet.taskmanager.edit.presentation

import android.view.View
import androidx.annotation.StringRes
import net.rusnet.taskmanager.commons.domain.model.TaskType

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
    val endTime: String
) {
    init {
        fun isVisibilityValueValid(value: Int) = (value == View.GONE || value == View.VISIBLE)

        if (!isVisibilityValueValid(addDateButtonVisibility))
            throw IllegalArgumentException("addDateButtonVisibility should be either View.GONE or View.Visible")
        if (!isVisibilityValueValid(dateLayoutVisibility))
            throw IllegalArgumentException("dateLayoutVisibility should be either View.GONE or View.Visible")
        if (!isVisibilityValueValid(additionalDatePickersVisibility))
            throw IllegalArgumentException("additionalDatePickersVisibility should be either View.GONE or View.Visible")
    }
}