package net.rusnet.taskmanager.commons.app

import android.app.Activity
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

    fun navigateToEdit(task: Task? = null) {
        if (task == null) {
            activity().startActivity(EditActivity.getIntentForNewTask(activity()))
        } else {
            activity().startActivity(EditActivity.getIntentForExistingTask(activity(), task))
        }
    }

}