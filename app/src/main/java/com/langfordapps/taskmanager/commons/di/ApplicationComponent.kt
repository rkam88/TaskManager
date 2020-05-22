package com.langfordapps.taskmanager.commons.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import com.langfordapps.taskmanager.commons.app.Router
import com.langfordapps.taskmanager.commons.presentation.ViewModelFactory
import com.langfordapps.taskmanager.edit.presentation.EditViewModel
import com.langfordapps.taskmanager.tasksdisplay.presentation.TasksDisplayViewModel
import javax.inject.Singleton

@Singleton
@Component(modules = [ApplicationModule::class])
interface ApplicationComponent {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance applicationContext: Context): ApplicationComponent
    }

    val router: Router
    val taskDisplayViewModelFactory: ViewModelFactory<TasksDisplayViewModel>
    val editViewModelFactory: ViewModelFactory<EditViewModel>

}