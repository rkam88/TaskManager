package com.langfordapps.taskmanager.task_storage.api.domain

import com.langfordapps.taskmanager.task_storage.api.domain.model.BaseFilter
import com.langfordapps.taskmanager.task_storage.api.domain.model.Task

interface TaskDataSource {
    suspend fun saveTask(taskToSave: Task): Long
    suspend fun getTasksCount(filter: BaseFilter): Long
    suspend fun getTasks(filter: BaseFilter): List<Task>
    suspend fun getTaskById(taskId: Long): Task
    suspend fun markTaskAsCompleted(taskId: Long)
    suspend fun deleteAllCompletedTasks()
    suspend fun deleteTasks(taskIdList: List<Long>)
    suspend fun getAllIncompleteTasks(): List<Task>
    suspend fun addTasks(tasks: List<Task>)
}