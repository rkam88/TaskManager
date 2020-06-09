package com.langfordapps.taskmanager.tasksdisplay.domain

import com.langfordapps.taskmanager.commons.data.TasksDataSource
import com.langfordapps.taskmanager.taskalarm.TaskAlarmService.TaskAlarmServiceHandler
import com.langfordapps.taskmanager.taskalarm.TaskAlarmServiceActions
import dagger.Reusable
import javax.inject.Inject

@Reusable
class MarkTaskAsCompletedUseCase @Inject constructor(
    private val taskAlarmServiceHandler: TaskAlarmServiceHandler,
    private val tasksDataSource: TasksDataSource
) {

    suspend fun execute(taskId: Long) {
        tasksDataSource.markTaskAsCompleted(taskId)
        taskAlarmServiceHandler.enqueueWork(TaskAlarmServiceActions.REMOVE_ONE, taskId)
    }

}