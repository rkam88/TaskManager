package com.langfordapps.taskmanager.taskalarm.domain

import com.langfordapps.taskmanager.commons.data.TasksDataSource
import com.langfordapps.taskmanager.commons.domain.model.Task
import dagger.Reusable
import javax.inject.Inject

@Reusable
class GetAllIncompleteTasksUseCase @Inject constructor(private val tasksDataSource: TasksDataSource) {

    suspend fun execute(): List<Task> {
        return tasksDataSource.getAllIncompleteTasks()
    }

}