package com.langfordapps.taskmanager.app

import com.langfordapps.taskmanager.app.AppScreen.TasksDisplayScreen
import com.langfordapps.taskmanager.tasks_display.TasksDisplayParent
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.parameter.parametersOf

interface AppScreenFactory {
    fun getTasksDisplayScreen(parent: TasksDisplayParent): TasksDisplayScreen
}

class AppScreenFactoryImpl : AppScreenFactory, KoinComponent {

    override fun getTasksDisplayScreen(
        parent: TasksDisplayParent
    ): TasksDisplayScreen = TasksDisplayScreen(
        bloc = get { parametersOf(parent) }
    )

}