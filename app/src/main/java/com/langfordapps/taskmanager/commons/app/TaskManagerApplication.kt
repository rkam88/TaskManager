package com.langfordapps.taskmanager.commons.app

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class TaskManagerApplication : Application() {

    @Inject
    lateinit var router: Router

    override fun onCreate() {
        super.onCreate()

        registerActivityLifecycleCallbacks(router)
    }

}
