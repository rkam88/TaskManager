package net.rusnet.taskmanager.tasksdisplay.presentation

import android.view.View
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import net.rusnet.taskmanager.R
import net.rusnet.taskmanager.tasksdisplay.presentation.TasksDisplayState.*

enum class TasksDisplayState(
    @StringRes val toolbarTitle: Int,
    @IdRes val navigationViewMenuId: Int,
    val addButtonVisibility: Int
) {
    INBOX         (R.string.inbox,         R.id.nav_inbox,         View.VISIBLE),
    NEXT_ACTIONS  (R.string.next_actions,  R.id.nav_next_actions,  View.VISIBLE),
    CALENDAR      (R.string.calendar,      R.id.nav_calendar,      View.VISIBLE),
    WAITING_FOR   (R.string.waiting_for,   R.id.nav_waiting_for,   View.VISIBLE),
    SOMEDAY_MAYBE (R.string.someday_maybe, R.id.nav_someday_maybe, View.VISIBLE),
    COMPLETED     (R.string.completed,     R.id.nav_completed,     View.GONE),
    DELETED       (R.string.deleted,       R.id.nav_deleted,       View.GONE)
}

fun @receiver:IdRes Int.getTasksDisplayState(): TasksDisplayState {
    return values().find { it.navigationViewMenuId == this }
        ?: throw IllegalArgumentException("The resource ID must correspond to an ID from the navigation drawer menu!")
}
