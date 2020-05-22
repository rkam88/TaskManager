package com.langfordapps.taskmanager.tasksdisplay.domain

import android.content.Context
import com.langfordapps.taskmanager.commons.data.TasksDataSource
import com.langfordapps.taskmanager.taskalarm.TaskAlarmService
import com.langfordapps.taskmanager.taskalarm.TaskAlarmServiceActions
import dagger.Reusable
import javax.inject.Inject

@Reusable
class MarkTaskAsCompletedUseCase @Inject constructor(
    private val applicationContext: Context,
    private val tasksDataSource: TasksDataSource
) {

    suspend fun execute(taskId: Long) {
        tasksDataSource.markTaskAsCompleted(taskId)
        TaskAlarmService.enqueueWork(applicationContext, TaskAlarmServiceActions.REMOVE_ONE, taskId)
    }

}