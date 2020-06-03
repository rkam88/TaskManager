package com.langfordapps.taskmanager.commons.presentation

import java.text.DateFormat
import java.util.Locale

object DateFormatHelper {

    @JvmStatic
    fun formatDate(dateInMillis: Long): String {
        return DateFormat.getDateInstance(DateFormat.SHORT, Locale.getDefault()).format(dateInMillis)
    }

    @JvmStatic
    fun formatTime(dateInMillis: Long): String {
        return DateFormat.getTimeInstance(DateFormat.SHORT, Locale.getDefault()).format(dateInMillis)
    }

}