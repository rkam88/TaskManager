package net.rusnet.taskmanager.tasksdisplay.domain

import dagger.Reusable
import net.rusnet.taskmanager.commons.data.TasksDataSource
import net.rusnet.taskmanager.commons.domain.BaseFilter
import javax.inject.Inject

@Reusable
class GetTasksCountUseCase @Inject constructor(private val tasksDataSource: TasksDataSource) {

    suspend fun execute(filter: BaseFilter): Long {
        return tasksDataSource.getTasksCount(filter)
    }
}