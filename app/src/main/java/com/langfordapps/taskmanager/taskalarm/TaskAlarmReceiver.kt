package com.langfordapps.taskmanager.taskalarm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.langfordapps.taskmanager.R
import com.langfordapps.taskmanager.tasksdisplay.presentation.TasksDisplayActivity

private const val CHANEL_ID = "Channel_1"
private const val NO_VALUE = -1

class TaskAlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        createNotificationChannel(context)

        val notificationId = intent.getIntExtra(EXTRA_ID, NO_VALUE)
        if (notificationId == NO_VALUE) throw IllegalArgumentException("Must provide an id")

        val startAppIntent = Intent(context, TasksDisplayActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            startAppIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notification = NotificationCompat.Builder(context, CHANEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(context.getString(R.string.reminder))
            .setContentText(requireNotNull(intent.getStringExtra(EXTRA_NOTIFICATION_TEXT)))
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        notificationManager.notify(notificationId, notification)
    }

    private fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                CHANEL_ID,
                context.getString(R.string.task_alarm_channel_name),
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = context.getString(R.string.task_alarm_channel_description)
            }

            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            notificationManager.createNotificationChannel(notificationChannel)
        }
    }

    companion object {
        private const val EXTRA_ID = "EXTRA_ID"
        private const val EXTRA_NOTIFICATION_TEXT = "EXTRA_NOTIFICATION_TEXT"

        fun getNotificationIntent(context: Context, id: Int, notificationText: String): Intent {
            return Intent(context, TaskAlarmReceiver::class.java).apply {
                putExtra(EXTRA_ID, id)
                putExtra(EXTRA_NOTIFICATION_TEXT, notificationText)
            }
        }

        fun getNotificationRemovalIntent(context: Context, id: Int): Intent {
            return Intent(context, TaskAlarmReceiver::class.java).apply {
                putExtra(EXTRA_ID, id)
            }
        }

    }
}