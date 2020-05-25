package com.langfordapps.taskmanager.commons.data

import com.langfordapps.taskmanager.commons.domain.BaseFilter
import com.langfordapps.taskmanager.commons.domain.model.Task
import com.langfordapps.taskmanager.commons.domain.model.TaskType
import dagger.Reusable
import javax.inject.Inject

@Reusable
class TasksDataSource @Inject constructor(private val taskDao: TaskDao) {

    suspend fun saveTask(taskToSave: Task): Long {
        return taskDao.insertTask(taskToSave)
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

    suspend fun deleteTasks(taskIdList: List<Long>) {
        taskDao.deleteTasks(taskIdList)
    }

    suspend fun getAllIncompleteTasks(): List<Task> {
        return taskDao.getTasksExcept(TaskType.COMPLETED)
    }

    suspend fun addTasks(tasks: List<Task>) {
        taskDao.insertTasks(tasks)
    }

}