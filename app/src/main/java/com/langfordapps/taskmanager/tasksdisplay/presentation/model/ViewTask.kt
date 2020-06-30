package com.langfordapps.taskmanager.tasksdisplay.presentation.model

import androidx.annotation.ColorRes
import com.langfordapps.taskmanager.commons.presentation.ViewVisibility

data class ViewTask(
    val taskId: Long,
    val name: String,
    val dateViewVisibility: ViewVisibility,
    val date: String?,
    @ColorRes val dateColor: Int,
    var isSelectedForDeletion: Boolean = false,
    val alarmViewVisibility: ViewVisibility,
    val alarmDate: String?,
    @ColorRes val alarmColor: Int
)