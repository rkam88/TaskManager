package net.rusnet.taskmanager.tasksdisplay.domain

import dagger.Reusable
import net.rusnet.taskmanager.commons.data.TasksDataSource
import javax.inject.Inject

@Reusable
class MarkTaskAsCompletedUseCase @Inject constructor(private val tasksDataSource: TasksDataSource) {

    suspend fun execute(taskId: Long) {
        tasksDataSource.markTaskAsCompleted(taskId)
    }

}