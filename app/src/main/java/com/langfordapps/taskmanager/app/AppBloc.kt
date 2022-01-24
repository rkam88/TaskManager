package com.langfordapps.taskmanager.app

import com.langfordapps.taskmanager.core.bloc.BaseAction
import com.langfordapps.taskmanager.core.bloc.BaseState
import com.langfordapps.taskmanager.core.bloc.Bloc
import com.langfordapps.taskmanager.tasks_display.TasksDisplayBloc
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

interface AppBloc : Bloc {
    val currentScreen: MutableStateFlow<AppScreen>
    val action: Flow<AppAction>
    fun onBackPressed()
}

sealed interface AppScreen : BaseState, Bloc {
    class TasksDisplayScreen(
        private val bloc: TasksDisplayBloc
    ) : AppScreen, TasksDisplayBloc by bloc
}

sealed interface AppAction : BaseAction {
    object OnBackPressed : AppAction
}