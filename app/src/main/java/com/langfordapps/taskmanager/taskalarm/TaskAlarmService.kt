package com.langfordapps.taskmanager.taskalarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.JobIntentService
import com.langfordapps.taskmanager.R
import com.langfordapps.taskmanager.commons.app.injector
import com.langfordapps.taskmanager.commons.domain.model.Task
import com.langfordapps.taskmanager.commons.extensions.exhaustive
import com.langfordapps.taskmanager.commons.extensions.getTaskDatesAsString
import com.langfordapps.taskmanager.commons.extensions.hasDates
import com.langfordapps.taskmanager.taskalarm.TaskAlarmServiceActions.REMOVE_ONE
import com.langfordapps.taskmanager.taskalarm.TaskAlarmServiceActions.UPDATE_ALL
import com.langfordapps.taskmanager.taskalarm.TaskAlarmServiceActions.UPDATE_ONE
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

private const val NO_TASK_ID = -1L
private const val MAX_INT_LENGTH = 1000000000

class TaskAlarmService : JobIntentService() {

    private val getTaskByIdUseCase by lazy { injector.getTaskByIdUseCase }
    private val getAllIncompleteTasksUseCase by lazy { injector.getAllIncompleteTasksUseCase }

    override fun onHandleWork(intent: Intent) {
        val action = intent.getSerializableExtra(EXTRA_ACTION) as TaskAlarmServiceActions
        val intentTaskId = intent.getLongExtra(EXTRA_TASK_ID, NO_TASK_ID)
        if (intentTaskId == NO_TASK_ID && action != UPDATE_ALL) return
        @Suppress("IMPLICIT_CAST_TO_ANY")
        when (action) {
            UPDATE_ONE -> GlobalScope.launch { updateTaskAlarm(getTaskByIdUseCase.execute(intentTaskId)) }
            UPDATE_ALL -> GlobalScope.launch {
                val taskList = getAllIncompleteTasksUseCase.execute()
                for (task in taskList) {
                    if (task.alarmDate != null) updateTaskAlarm(task)
                }
            }
            REMOVE_ONE -> removeTaskAlarm(intentTaskId)
        }.exhaustive
    }

    private fun updateTaskAlarm(task: Task) {
        val pendingIntendId = (task.id % MAX_INT_LENGTH).toInt()
        val notificationText = if (task.hasDates()) {
            resources.getString(
                R.string.task_alarm_task_description_w_date,
                task.name,
                task.getTaskDatesAsString(applicationContext)
            )
        } else {
            resources.getString(
                R.string.task_alarm_task_description_wo_date,
                task.name
            )
        }

        val intent = TaskAlarmReceiver.getNotificationIntent(applicationContext, pendingIntendId, notificationText)
        val pendingIntent = PendingIntent.getBroadcast(
            this,
            pendingIntendId,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

        if (task.alarmDate != null && task.alarmDate > System.currentTimeMillis()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, task.alarmDate, pendingIntent)
            } else {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, task.alarmDate, pendingIntent)
            }
        } else {
            alarmManager.cancel(pendingIntent)
        }
    }

    private fun removeTaskAlarm(taskId: Long) {
        val pendingIntendId = (taskId % MAX_INT_LENGTH).toInt()
        val intent = TaskAlarmReceiver.getNotificationRemovalIntent(applicationContext, pendingIntendId)
        val pendingIntent = PendingIntent.getBroadcast(
            this,
            pendingIntendId,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val alarmManager = this.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(pendingIntent)
    }

    companion object {
        private const val EXTRA_ACTION = "EXTRA_ACTION"
        private const val EXTRA_TASK_ID = "EXTRA_TASK_ID"
        private const val JOB_ID = 1313

        fun enqueueWork(context: Context, action: TaskAlarmServiceActions, taskId: Long? = null) {
            val workIntent = Intent().apply {
                putExtra(EXTRA_ACTION, action)
                putExtra(EXTRA_TASK_ID, taskId)
            }
            enqueueWork(context, TaskAlarmService::class.java, JOB_ID, workIntent)
        }
    }
}

enum class TaskAlarmServiceActions {
    UPDATE_ONE,
    UPDATE_ALL,
    REMOVE_ONE
}