package net.rusnet.taskmanager.tasksdisplay.domain

import dagger.Reusable
import net.rusnet.taskmanager.commons.data.TaskDao
import net.rusnet.taskmanager.commons.domain.BaseFilter
import javax.inject.Inject

@Reusable
class GetTasksCountUseCase @Inject constructor(private val taskDao: TaskDao) {

    suspend fun execute(filter: BaseFilter): Long {
        return taskDao.getTasksCount(
            isInTrash = filter.isInTrash,
            isCompleted = filter.isCompleted,
            type = filter.type,
            hasDates = filter.hasDates
        )
    }
}