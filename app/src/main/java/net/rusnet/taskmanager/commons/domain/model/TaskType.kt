package net.rusnet.taskmanager.commons.domain.model

import androidx.annotation.StringRes
import net.rusnet.taskmanager.R

enum class TaskType(
    val spinnerPosition: Int,
    @StringRes val nameInUi: Int
) {
    INBOX(0, R.string.inbox),
    ACTIVE(1, R.string.active),
    WAITING_FOR(2, R.string.waiting_for),
    SOMEDAY_MAYBE(3, R.string.someday_maybe),
    COMPLETED(4, R.string.completed)
}