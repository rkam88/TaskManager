package com.langfordapps.taskmanager.app

import com.langfordapps.taskmanager.core.bloc.BaseState
import com.langfordapps.taskmanager.core.bloc.Bloc
import com.langfordapps.taskmanager.tasks_display.TasksDisplayBloc
import kotlinx.coroutines.flow.MutableStateFlow

interface AppBloc : Bloc {
    val currentScreen: MutableStateFlow<AppScreen>
}

sealed interface AppScreen : BaseState, Bloc {
    class TasksDisplayScreen(
        private val bloc: TasksDisplayBloc
    ) : AppScreen, TasksDisplayBloc by bloc
}