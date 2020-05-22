package net.rusnet.taskmanager.tasksdisplay.domain

import net.rusnet.taskmanager.commons.data.TasksDataSource
import javax.inject.Inject

class DeleteCompletedTasksUseCase @Inject constructor(private val tasksDataSource: TasksDataSource) {

    suspend fun execute() {
        tasksDataSource.deleteAllCompletedTasks()
    }

}