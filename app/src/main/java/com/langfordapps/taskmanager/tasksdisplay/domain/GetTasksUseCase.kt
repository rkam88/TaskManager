package com.langfordapps.taskmanager.tasksdisplay.domain

import com.langfordapps.taskmanager.commons.data.TasksDataSource
import com.langfordapps.taskmanager.commons.domain.BaseFilter
import com.langfordapps.taskmanager.commons.domain.model.Task
import javax.inject.Inject

class GetTasksUseCase @Inject constructor(private val tasksDataSource: TasksDataSource) {

    suspend fun execute(filter: BaseFilter): List<Task> {
        return tasksDataSource.getTasks(filter)
    }

}