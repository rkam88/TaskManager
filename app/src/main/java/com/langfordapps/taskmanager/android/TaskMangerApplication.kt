package com.langfordapps.taskmanager.android

import android.app.Application
import com.langfordapps.taskmanager.di.appModule
import com.langfordapps.taskmanager.di.tasksDisplayModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

@Suppress("unused")
class TaskManagerApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        initKoin()
    }

    private fun initKoin() {
        startKoin {
            androidContext(this@TaskManagerApplication)
            modules(
                listOf(
                    appModule,
                    tasksDisplayModule,
                )
            )
        }
    }

}