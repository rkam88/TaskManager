package com.langfordapps.taskmanager.taskalarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class InitAlarmBootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == "android.intent.action.BOOT_COMPLETED") {
            TaskAlarmService.enqueueWork(context, TaskAlarmServiceActions.UPDATE_ALL)
        }
    }
}