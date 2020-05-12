package net.rusnet.taskmanager.commons.app

import android.app.Activity
import net.rusnet.taskmanager.commons.domain.model.TaskType
import net.rusnet.taskmanager.edit.presentation.EditActivity
import net.rusnet.taskmanager_old.commons.domain.model.Task
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

    fun navigateToEdit(existingTask: Task) {
        activity().startActivity(EditActivity.getIntentForExistingTask(activity(), existingTask))
    }

    fun navigateToEdit(newTaskType: TaskType) {
        activity().startActivity(EditActivity.getIntentForNewTask(activity(), newTaskType))
    }

}