package net.rusnet.taskmanager.tasksdisplay.domain

import dagger.Reusable
import net.rusnet.taskmanager.commons.data.TasksDataSource
import net.rusnet.taskmanager.commons.domain.BaseFilter
import net.rusnet.taskmanager.commons.domain.model.Task
import javax.inject.Inject

@Reusable
class GetTasksUseCase @Inject constructor(private val tasksDataSource: TasksDataSource) {

    suspend fun execute(filter: BaseFilter): List<Task> {
        return tasksDataSource.getTasks(filter)
    }

}