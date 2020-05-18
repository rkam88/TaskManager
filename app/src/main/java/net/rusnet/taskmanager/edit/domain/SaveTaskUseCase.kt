package net.rusnet.taskmanager.edit.domain

import dagger.Reusable
import net.rusnet.taskmanager.commons.data.TaskDao
import net.rusnet.taskmanager.commons.domain.model.Task
import javax.inject.Inject

@Reusable
class SaveTaskUseCase @Inject constructor(private val taskDao: TaskDao) {

    suspend fun execute(taskToSave: Task) {
        taskDao.insertTask(taskToSave)
    }

}