package com.langfordapps.taskmanager.task_storage.internal.data

import com.langfordapps.taskmanager.task_storage.api.domain.TaskDataSource
import com.langfordapps.taskmanager.task_storage.api.domain.model.BaseFilter
import com.langfordapps.taskmanager.task_storage.api.domain.model.Task
import com.langfordapps.taskmanager.task_storage.api.domain.model.TaskStatus

internal class TaskDataSourceImpl(
    private val taskDao: TaskDao,
) : TaskDataSource {

    override suspend fun saveTask(taskToSave: Task): Long {
        return taskDao.insertTask(taskToSave)
    }

    override suspend fun getTasksCount(filter: BaseFilter): Long {
        return taskDao.getTasksCount(
            status = filter.status,
            checkDates = filter.hasDates != null,
            hasDates = filter.hasDates
        )
    }

    override suspend fun getTasks(filter: BaseFilter): List<Task> {
        return taskDao.getTasks(
            status = filter.status,
            checkDates = filter.hasDates != null,
            hasDates = filter.hasDates
        )
    }

    override suspend fun getTaskById(taskId: Long): Task {
        return taskDao.getTask(taskId)
    }

    override suspend fun markTaskAsCompleted(taskId: Long) {
        taskDao.updateTaskStatus(taskId, TaskStatus.COMPLETED)
    }

    override suspend fun deleteAllCompletedTasks() {
        taskDao.deleteTasks(TaskStatus.COMPLETED)
    }

    override suspend fun deleteTasks(taskIdList: List<Long>) {
        taskDao.deleteTasks(taskIdList)
    }

    override suspend fun getAllIncompleteTasks(): List<Task> {
        return taskDao.getTasksExcept(TaskStatus.COMPLETED)
    }

    override suspend fun addTasks(tasks: List<Task>) {
        taskDao.insertTasks(tasks)
    }

}