package com.langfordapps.taskmanager.edit.domain

import android.content.Context
import com.langfordapps.taskmanager.commons.data.TasksDataSource
import com.langfordapps.taskmanager.commons.domain.model.Task
import com.langfordapps.taskmanager.taskalarm.TaskAlarmService
import com.langfordapps.taskmanager.taskalarm.TaskAlarmServiceActions
import dagger.Reusable
import javax.inject.Inject

@Reusable
class SaveTaskUseCase @Inject constructor(
    private val applicationContext: Context,
    private val tasksDataSource: TasksDataSource
) {

    suspend fun execute(taskToSave: Task) {
        val taskId = tasksDataSource.saveTask(taskToSave)
        TaskAlarmService.enqueueWork(applicationContext, TaskAlarmServiceActions.UPDATE_ONE, taskId)
    }

}