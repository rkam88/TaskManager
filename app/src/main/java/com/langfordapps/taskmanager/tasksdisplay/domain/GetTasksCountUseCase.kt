package com.langfordapps.taskmanager.tasksdisplay.domain

import com.langfordapps.taskmanager.commons.data.TasksDataSource
import com.langfordapps.taskmanager.commons.domain.BaseFilter
import javax.inject.Inject

class GetTasksCountUseCase @Inject constructor(private val tasksDataSource: TasksDataSource) {

    suspend fun execute(filter: BaseFilter): Long {
        return tasksDataSource.getTasksCount(filter)
    }
}