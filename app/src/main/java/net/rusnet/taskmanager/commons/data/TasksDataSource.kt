package net.rusnet.taskmanager.commons.data

import dagger.Reusable
import net.rusnet.taskmanager.commons.domain.BaseFilter
import net.rusnet.taskmanager.commons.domain.model.Task
import net.rusnet.taskmanager.commons.domain.model.TaskType
import javax.inject.Inject

@Reusable
class TasksDataSource @Inject constructor(private val taskDao: TaskDao) {

    suspend fun saveTask(taskToSave: Task) {
        taskDao.insertTask(taskToSave)
    }

    suspend fun getTasksCount(filter: BaseFilter): Long {
        return taskDao.getTasksCount(
            taskType = filter.taskType,
            checkDates = filter.hasDates != null,
            hasDates = filter.hasDates
        )
    }

    suspend fun getTasks(filter: BaseFilter): List<Task> {
        return taskDao.getTasks(
            taskType = filter.taskType,
            checkDates = filter.hasDates != null,
            hasDates = filter.hasDates
        )
    }

    suspend fun getTaskById(taskId: Long) = taskDao.getTask(taskId)

    suspend fun markTaskAsCompleted(taskId: Long) {
        taskDao.updateTaskType(taskId, TaskType.COMPLETED)
    }

    suspend fun deleteAllCompletedTasks() {
        taskDao.deleteTasks(TaskType.COMPLETED)
    }

}