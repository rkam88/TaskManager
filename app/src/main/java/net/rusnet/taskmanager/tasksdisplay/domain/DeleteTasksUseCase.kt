package net.rusnet.taskmanager.tasksdisplay.domain

import net.rusnet.taskmanager.commons.data.TasksDataSource
import javax.inject.Inject

class DeleteTasksUseCase @Inject constructor(private val tasksDataSource: TasksDataSource) {

    suspend fun execute(taskIdList: List<Long>) {
        tasksDataSource.deleteTasks(taskIdList)
    }

}