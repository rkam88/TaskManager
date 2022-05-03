package com.langfordapps.taskmanager.task_storage.api.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
enum class TaskStatus : Parcelable {
    INBOX, ACTIVE, WAITING_FOR, SOMEDAY_MAYBE, COMPLETED
}