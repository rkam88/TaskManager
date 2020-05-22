package com.langfordapps.taskmanager.tasksdisplay.domain

import com.langfordapps.taskmanager.commons.data.TasksDataSource
import javax.inject.Inject

class DeleteTasksUseCase @Inject constructor(private val tasksDataSource: TasksDataSource) {

    suspend fun execute(taskIdList: List<Long>) {
        tasksDataSource.deleteTasks(taskIdList)
    }

}