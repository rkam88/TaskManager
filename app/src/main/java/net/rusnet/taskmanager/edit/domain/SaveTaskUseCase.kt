package net.rusnet.taskmanager.edit.domain

import dagger.Reusable
import net.rusnet.taskmanager.commons.data.TasksDataSource
import net.rusnet.taskmanager.commons.domain.model.Task
import javax.inject.Inject

@Reusable
class SaveTaskUseCase @Inject constructor(private val tasksDataSource: TasksDataSource) {

    suspend fun execute(taskToSave: Task) {
        tasksDataSource.saveTask(taskToSave)
    }

}