package com.langfordapps.taskmanager.tasksdisplay.domain

import com.langfordapps.taskmanager.commons.data.TasksDataSource
import com.langfordapps.taskmanager.tasksdisplay.data.TutorialTasksProvider
import javax.inject.Inject

class AddTutorialTasksUseCase @Inject constructor(
    private val tutorialTasksProvider: TutorialTasksProvider,
    private val tasksDataSource: TasksDataSource
) {

    suspend fun execute() {
        val tasks = tutorialTasksProvider.getTutorialTasks()
        tasksDataSource.addTasks(tasks)
    }

}