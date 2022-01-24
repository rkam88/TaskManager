package com.langfordapps.taskmanager.di

import com.langfordapps.taskmanager.tasks_display.TasksDisplayBloc
import com.langfordapps.taskmanager.tasks_display.TasksDisplayBlocImpl
import com.langfordapps.taskmanager.tasks_display.TasksDisplayParent
import org.koin.dsl.module

val tasksDisplayModule = module {
    factory<TasksDisplayBloc> { (parent: TasksDisplayParent) ->
        TasksDisplayBlocImpl(
            parent = parent,
            tasksRepository = get(),
        )
    }
}