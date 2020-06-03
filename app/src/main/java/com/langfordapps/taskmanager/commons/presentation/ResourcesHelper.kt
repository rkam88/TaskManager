package com.langfordapps.taskmanager.commons.presentation

import android.content.Context
import com.langfordapps.taskmanager.R
import dagger.Reusable
import javax.inject.Inject

@Reusable
class ResourcesHelper @Inject constructor(private val applicationContext: Context) {

    val deleteCompletedTasksDialogTitle
        get() = applicationContext.getString(R.string.delete_completed_dialog_title)

    fun getSelectedTasksMessage(numberOfTasks: Int): String {
        return applicationContext.resources.getQuantityString(
            R.plurals.n_tasks_selected,
            numberOfTasks,
            numberOfTasks
        )
    }

    fun getDeleteTasksDialogTitle(numberOfTasks: Int): String {
        return applicationContext.resources.getQuantityString(
            R.plurals.delete_tasks_dialog_title,
            numberOfTasks,
            numberOfTasks
        )
    }

}