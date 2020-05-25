package com.langfordapps.taskmanager.tasksdisplay.data

import android.content.Context
import com.langfordapps.taskmanager.R
import com.langfordapps.taskmanager.commons.domain.model.Task
import com.langfordapps.taskmanager.commons.domain.model.TaskType
import com.langfordapps.taskmanager.commons.extensions.setDatesToAllDayAndCopy
import java.util.Calendar
import javax.inject.Inject

private const val FIVE_MINUTES_IN_MS = 5 * 60 * 1000L
private const val THIRTY_MINUTES_IN_MS = 30 * 60 * 1000L
private const val ONE_HOUR_IN_MS = 60 * 60 * 1000L
private const val TWO_HOURS_IN_MS = 2 * 60 * 60 * 1000L

class TutorialTasksProvider @Inject constructor(private val context: Context) {

    fun getTutorialTasks(): List<Task> {
        val tutorialTasks = mutableListOf<Task>()

        val taskNames = context.resources.getStringArray(R.array.tutorial_tasks_array)


        for (taskNumber in 0..3) {
            tutorialTasks.add(Task(name = taskNames[taskNumber], taskType = TaskType.INBOX))
        }

        val thisHourStart = Calendar.getInstance().apply {
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis

        tutorialTasks.add(
            Task(
                name = taskNames[4],
                taskType = TaskType.ACTIVE,
                startDate = thisHourStart,
                endDate = thisHourStart,
                alarmDate = thisHourStart - FIVE_MINUTES_IN_MS
            )
        )

        tutorialTasks.add(
            Task(
                name = taskNames[5],
                taskType = TaskType.ACTIVE,
                startDate = thisHourStart + ONE_HOUR_IN_MS,
                endDate = thisHourStart + ONE_HOUR_IN_MS
            )
        )

        tutorialTasks.add(
            Task(
                name = taskNames[6],
                taskType = TaskType.ACTIVE,
                startDate = thisHourStart + ONE_HOUR_IN_MS,
                endDate = thisHourStart + ONE_HOUR_IN_MS + THIRTY_MINUTES_IN_MS
            )
        )

        tutorialTasks.add(
            Task(
                name = taskNames[7],
                taskType = TaskType.ACTIVE,
                startDate = thisHourStart + TWO_HOURS_IN_MS,
                endDate = thisHourStart + TWO_HOURS_IN_MS,
                alarmDate = thisHourStart + TWO_HOURS_IN_MS - FIVE_MINUTES_IN_MS
            )
        )

        val thisTimeTomorrow = Calendar.getInstance().apply {
            set(Calendar.DAY_OF_YEAR, get(Calendar.DAY_OF_YEAR) + 1)
        }.timeInMillis
        val allDay = Task(
            name = taskNames[8],
            taskType = TaskType.ACTIVE,
            startDate = thisTimeTomorrow,
            endDate = thisTimeTomorrow
        )
        tutorialTasks.add(allDay.setDatesToAllDayAndCopy())

        tutorialTasks.add(
            Task(
                name = taskNames[9],
                taskType = TaskType.COMPLETED,
                startDate = thisHourStart,
                endDate = thisHourStart,
                alarmDate = thisHourStart - FIVE_MINUTES_IN_MS
            )
        )

        return tutorialTasks
    }

}