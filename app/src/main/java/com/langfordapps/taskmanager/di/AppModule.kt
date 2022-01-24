package com.langfordapps.taskmanager.di

import com.langfordapps.taskmanager.android.MainViewModel
import com.langfordapps.taskmanager.app.AppBloc
import com.langfordapps.taskmanager.app.AppBlocImpl
import com.langfordapps.taskmanager.app.AppScreenFactory
import com.langfordapps.taskmanager.app.AppScreenFactoryImpl
import com.langfordapps.taskmanager.common.data.FakeTaskRepository
import com.langfordapps.taskmanager.common.data.TasksRepository
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel {
        MainViewModel(
            appBloc = get()
        )
    }

    factory<AppBloc> {
        AppBlocImpl(
            factory = get()
        )
    }

    factory<AppScreenFactory> {
        AppScreenFactoryImpl()
    }

    single<TasksRepository> {
        FakeTaskRepository(
            context = get()
        )
    }
}