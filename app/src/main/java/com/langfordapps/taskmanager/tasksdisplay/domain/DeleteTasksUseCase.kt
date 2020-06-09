package com.langfordapps.taskmanager.tasksdisplay.domain

import com.langfordapps.taskmanager.commons.data.TasksDataSource
import com.langfordapps.taskmanager.taskalarm.TaskAlarmService
import com.langfordapps.taskmanager.taskalarm.TaskAlarmServiceActions
import javax.inject.Inject

class DeleteTasksUseCase @Inject constructor(
    private val taskAlarmServiceHandler: TaskAlarmService.TaskAlarmServiceHandler,
    private val tasksDataSource: TasksDataSource
) {

    suspend fun execute(taskIdList: List<Long>) {
        tasksDataSource.deleteTasks(taskIdList)
        for (taskId in taskIdList) {
            taskAlarmServiceHandler.enqueueWork(TaskAlarmServiceActions.REMOVE_ONE, taskId)
        }
    }

}