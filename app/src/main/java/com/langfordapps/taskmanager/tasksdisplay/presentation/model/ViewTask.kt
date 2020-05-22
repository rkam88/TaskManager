package com.langfordapps.taskmanager.tasksdisplay.presentation.model

import android.view.View
import androidx.annotation.ColorRes

data class ViewTask(
    val taskId: Long,
    val name: String,
    val dateViewVisibility: Int,
    val date: String?,
    @ColorRes val dateColor: Int,
    var isSelectedForDeletion: Boolean = false
) {
    init {
        if (dateViewVisibility != View.GONE && dateViewVisibility != View.VISIBLE) {
            throw IllegalArgumentException("dateVisibility should be either View.GONE or View.Visible")
        }
    }
}