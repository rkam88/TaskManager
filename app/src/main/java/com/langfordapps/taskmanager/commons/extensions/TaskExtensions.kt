package com.langfordapps.taskmanager.commons.extensions

import android.content.Context
import android.text.format.DateFormat
import com.langfordapps.taskmanager.commons.domain.model.Task
import java.util.Calendar

private const val START_HOUR_OF_DAY = 0
private const val START_MINUTE = 0
private const val START_SECOND = 0
private const val START_MILLISECOND = 0
private const val END_HOUR_OF_DAY = 23
private const val END_MINUTE = 59
private const val END_SECOND = 59
private const val END_MILLISECOND = 999

fun Task.getOrInitStartDate() = startDate ?: Task.getInitialTaskDate()

fun Task.getOrInitEndDate() = endDate ?: Task.getInitialTaskDate()

fun Task.getOrInitAlarmDate() = alarmDate ?: startDate ?: Task.getInitialAlarmDate()

fun Task.Companion.getInitialTaskDate(): Long {
    val initialDate = Calendar.getInstance().apply { timeInMillis = System.currentTimeMillis() }
    initialDate.set(Calendar.HOUR_OF_DAY, START_HOUR_OF_DAY)
    initialDate.set(Calendar.MINUTE, START_MINUTE)
    initialDate.set(Calendar.SECOND, START_SECOND)
    initialDate.set(Calendar.MILLISECOND, START_MILLISECOND)
    return initialDate.timeInMillis
}

fun Task.Companion.getInitialAlarmDate(): Long {
    val initialDate = Calendar.getInstance().apply { timeInMillis = System.currentTimeMillis() }
    initialDate.set(Calendar.SECOND, START_SECOND)
    initialDate.set(Calendar.MILLISECOND, START_MILLISECOND)
    return initialDate.timeInMillis
}

fun Task.setDatesToAllDayAndCopy(): Task {
    if (startDate == null || endDate == null) throw IllegalStateException("Can't set dates to all day on a task without dates!")
    val start = Calendar.getInstance().apply { timeInMillis = startDate }
    val end = Calendar.getInstance().apply { timeInMillis = endDate }
    start.set(Calendar.HOUR_OF_DAY, START_HOUR_OF_DAY)
    start.set(Calendar.MINUTE, START_MINUTE)
    start.set(Calendar.SECOND, START_SECOND)
    start.set(Calendar.MILLISECOND, START_MILLISECOND)
    end.set(Calendar.DAY_OF_YEAR, start.get(Calendar.DAY_OF_YEAR))
    end.set(Calendar.HOUR_OF_DAY, END_HOUR_OF_DAY)
    end.set(Calendar.MINUTE, END_MINUTE)
    end.set(Calendar.SECOND, END_SECOND)
    end.set(Calendar.MILLISECOND, END_MILLISECOND)
    return this.copy(startDate = start.timeInMillis, endDate = end.timeInMillis)
}

fun Task.hasDates() = startDate != null && endDate != null

fun Task.doStartAndEndDatesMatch() = startDate == endDate

fun Task.areDatesAllDay(): Boolean {
    if (startDate == null || endDate == null) return false
    val start = Calendar.getInstance().apply { timeInMillis = startDate }
    val end = Calendar.getInstance().apply { timeInMillis = endDate }
    return ((start.get(Calendar.DAY_OF_YEAR) == end.get(Calendar.DAY_OF_YEAR)) &&
            (start.get(Calendar.HOUR_OF_DAY) == START_HOUR_OF_DAY) &&
            (start.get(Calendar.MINUTE) == START_MINUTE) &&
            (start.get(Calendar.SECOND) == START_SECOND) &&
            (start.get(Calendar.MILLISECOND) == START_MILLISECOND) &&
            (end.get(Calendar.HOUR_OF_DAY) == END_HOUR_OF_DAY) &&
            (end.get(Calendar.MINUTE) == END_MINUTE) &&
            (end.get(Calendar.SECOND) == END_SECOND) &&
            (end.get(Calendar.MILLISECOND) == END_MILLISECOND))
}

fun Task.doStartAndEndDaysMatch(): Boolean {
    if (startDate == null || endDate == null) return false
    val start = Calendar.getInstance().apply { timeInMillis = startDate }
    val end = Calendar.getInstance().apply { timeInMillis = endDate }
    return (start.get(Calendar.DAY_OF_YEAR) == end.get(Calendar.DAY_OF_YEAR))
}

fun Task.doTimesMatchDayStart() = doesTimeMatchDayStart(startDate) && doesTimeMatchDayStart(endDate)

private fun doesTimeMatchDayStart(timeAsLong: Long?): Boolean {
    if (timeAsLong == null) return false
    val time = Calendar.getInstance().apply { timeInMillis = timeAsLong }
    return ((time.get(Calendar.HOUR_OF_DAY) == START_HOUR_OF_DAY) &&
            (time.get(Calendar.MINUTE) == START_MINUTE) &&
            (time.get(Calendar.SECOND) == START_SECOND) &&
            (time.get(Calendar.MILLISECOND) == START_MILLISECOND)
            )
}

fun Task.isOverdue() = (System.currentTimeMillis() > endDate ?: Long.MAX_VALUE)

fun Task.getTaskDatesAsString(applicationContext: Context): String? {
    return if (hasDates()) {
        val startDay = DateFormat.getDateFormat(applicationContext).format(startDate)
        val startTime = DateFormat.getTimeFormat(applicationContext).format(startDate)
        val endDay = DateFormat.getDateFormat(applicationContext).format(endDate)
        val endTime = DateFormat.getTimeFormat(applicationContext).format(endDate)
        when {
            doStartAndEndDatesMatch() -> "$startDay, $startTime"
            areDatesAllDay() -> startDay
            doStartAndEndDaysMatch() -> "$startDay, $startTime - $endTime"
            doTimesMatchDayStart() -> "$startDay - $endDay"
            else -> "$startDay, $startTime - $endDay, $endTime"
        }
    } else {
        null
    }
}