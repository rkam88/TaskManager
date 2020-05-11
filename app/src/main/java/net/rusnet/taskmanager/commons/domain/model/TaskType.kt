package net.rusnet.taskmanager.commons.domain.model

import androidx.annotation.StringRes
import net.rusnet.taskmanager.R

enum class TaskType(
    @StringRes val uiName: Int
) {
    INBOX(R.string.inbox),
    ACTIVE(R.string.active),
    SOMEDAY_MAYBE(R.string.someday_maybe)
}