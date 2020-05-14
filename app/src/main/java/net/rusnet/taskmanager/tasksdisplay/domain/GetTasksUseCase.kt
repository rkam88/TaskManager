package net.rusnet.taskmanager.tasksdisplay.domain

import dagger.Reusable
import net.rusnet.taskmanager.commons.data.TaskDao
import net.rusnet.taskmanager.commons.domain.BaseFilter
import net.rusnet.taskmanager_old.commons.domain.model.Task
import javax.inject.Inject

@Reusable
class GetTasksUseCase @Inject constructor(private val taskDao: TaskDao) {

    suspend fun execute(filter: BaseFilter): List<Task> {
        return taskDao.getTasks(
            isInTrash = filter.isInTrash,
            isCompleted = filter.isCompleted,
            type = filter.type,
            hasDates = filter.hasDates
        )
    }

}