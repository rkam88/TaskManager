package com.langfordapps.taskmanager.edit.domain

import com.langfordapps.taskmanager.commons.data.TasksDataSource
import com.langfordapps.taskmanager.commons.domain.model.Task
import com.langfordapps.taskmanager.taskalarm.TaskAlarmService.TaskAlarmServiceHandler
import com.langfordapps.taskmanager.taskalarm.TaskAlarmServiceActions
import dagger.Reusable
import javax.inject.Inject

@Reusable
class SaveTaskUseCase @Inject constructor(
    private val taskAlarmServiceHandler: TaskAlarmServiceHandler,
    private val tasksDataSource: TasksDataSource
) {

    suspend fun execute(taskToSave: Task) {
        val taskId = tasksDataSource.saveTask(taskToSave)
        taskAlarmServiceHandler.enqueueWork(TaskAlarmServiceActions.UPDATE_ONE, taskId)
    }

}