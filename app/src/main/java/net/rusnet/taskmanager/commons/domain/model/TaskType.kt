package net.rusnet.taskmanager.commons.domain.model

import androidx.annotation.StringRes
import net.rusnet.taskmanager.R

enum class TaskType(
        val spinnerPosition: Int,
        @StringRes val nameInUi: Int
) {
    INBOX(0, R.string.inbox),
    ACTIVE(1, R.string.active),
    SOMEDAY_MAYBE(2, R.string.someday_maybe)
}