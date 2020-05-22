package com.langfordapps.taskmanager.commons.app

import android.app.Activity
import android.app.Application
import androidx.core.app.JobIntentService
import com.langfordapps.taskmanager.commons.di.ApplicationComponent
import com.langfordapps.taskmanager.commons.di.DaggerApplicationComponent

// can be overridden to provide a test component in tests
interface ContentProvider {
    val component: ApplicationComponent
}

class TaskManagerApplication : Application(), ContentProvider {

    override val component: ApplicationComponent by lazy {
        DaggerApplicationComponent
            .factory()
            .create(applicationContext)
    }

    override fun onCreate() {
        super.onCreate()

        registerActivityLifecycleCallbacks(component.router)
    }

}

val Activity.injector get() = (application as ContentProvider).component
val JobIntentService.injector get() = (application as ContentProvider).component