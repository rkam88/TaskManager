package com.langfordapps.taskmanager.tasksdisplay.presentation.model

import android.view.View
import androidx.annotation.ColorRes

data class ViewTask(
    val taskId: Long,
    val name: String,
    val dateViewVisibility: Int,
    val date: String?,
    @ColorRes val dateColor: Int,
    var isSelectedForDeletion: Boolean = false,
    val alarmViewVisibility: Int,
    val alarmDate: String?,
    @ColorRes val alarmColor: Int
) {
    init {
        fun validateVisibilityValue(value: Int, parameterName: String) {
            if (!(value == View.GONE || value == View.VISIBLE)) throw IllegalArgumentException("$parameterName should be either View.GONE or View.Visible")
        }

        validateVisibilityValue(dateViewVisibility, "dateViewVisibility")
        validateVisibilityValue(alarmViewVisibility, "alarmViewVisibility")
    }
}