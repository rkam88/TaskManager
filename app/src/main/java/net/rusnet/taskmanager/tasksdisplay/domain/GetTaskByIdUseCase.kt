package net.rusnet.taskmanager.tasksdisplay.domain

import net.rusnet.taskmanager.commons.data.TasksDataSource
import javax.inject.Inject

class GetTaskByIdUseCase @Inject constructor(private val tasksDataSource: TasksDataSource) {

    suspend fun execute(taskId: Long) = tasksDataSource.getTaskById(taskId)

}