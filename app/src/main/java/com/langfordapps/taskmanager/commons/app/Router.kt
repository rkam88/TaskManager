package com.langfordapps.taskmanager.commons.app

import android.app.Activity
import com.langfordapps.taskmanager.commons.domain.model.Task
import com.langfordapps.taskmanager.commons.domain.model.TaskType
import com.langfordapps.taskmanager.edit.presentation.EditActivity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Router @Inject constructor() : SimpleActivityLifecycleCallbacks {


    private var activity: Activity? = null

    override fun onActivityResumed(activity: Activity) {
        this.activity = activity
    }

    override fun onActivityPaused(activity: Activity) {
        this.activity = null
    }

    private fun activity() = activity ?: throw  IllegalStateException("Activity not ready")

    fun navigateToEdit(existingTask: Task, requestCode: Int) {
        activity().startActivityForResult(
            EditActivity.getIntentForExistingTask(activity(), existingTask),
            requestCode
        )
    }

    fun navigateToEdit(newTaskType: TaskType, showDates: Boolean, requestCode: Int) {
        activity().startActivityForResult(
            EditActivity.getIntentForNewTask(activity(), newTaskType, showDates),
            requestCode
        )
    }

}