package net.rusnet.taskmanager.commons.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import net.rusnet.taskmanager.commons.app.Router
import net.rusnet.taskmanager.commons.presentation.ViewModelFactory
import net.rusnet.taskmanager.edit.presentation.EditViewModel
import net.rusnet.taskmanager.tasksdisplay.presentation.TasksDisplayViewModel
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