package com.langfordapps.taskmanager.tasksdisplay.domain

import com.langfordapps.taskmanager.commons.data.TasksDataSource
import javax.inject.Inject

class GetTaskByIdUseCase @Inject constructor(private val tasksDataSource: TasksDataSource) {

    suspend fun execute(taskId: Long) = tasksDataSource.getTaskById(taskId)

}