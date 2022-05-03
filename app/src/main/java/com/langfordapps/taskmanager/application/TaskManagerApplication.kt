package com.langfordapps.taskmanager.application

import android.app.Application
import com.langfordapps.taskmanager.task_storage.api.di.taskStorageModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class TaskManagerApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        initKoin()
    }

    private fun initKoin() {
        startKoin {
            androidContext(this@TaskManagerApplication)
            modules(
                taskStorageModule,
            )
        }
    }

}