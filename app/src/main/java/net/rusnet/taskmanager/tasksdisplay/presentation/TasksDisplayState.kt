package net.rusnet.taskmanager.tasksdisplay.presentation

import android.view.View
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import net.rusnet.taskmanager.R
import net.rusnet.taskmanager.commons.domain.BaseFilter
import net.rusnet.taskmanager.commons.domain.BaseFilter.*
import net.rusnet.taskmanager.tasksdisplay.presentation.TasksDisplayState.*

enum class TasksDisplayState(
    @StringRes val toolbarTitle: Int,
    @IdRes val navigationViewMenuId: Int,
    val addButtonVisibility: Int,
    val baseFilter: BaseFilter
) {
    INBOX         (R.string.inbox,         R.id.nav_inbox,         View.VISIBLE, InboxFilter),
    NEXT_ACTIONS  (R.string.next_actions,  R.id.nav_next_actions,  View.VISIBLE, NextActionsFilter),
    CALENDAR      (R.string.calendar,      R.id.nav_calendar,      View.VISIBLE, CalendarFilter),
    WAITING_FOR   (R.string.waiting_for,   R.id.nav_waiting_for,   View.VISIBLE, WaitingForFilter),
    SOMEDAY_MAYBE (R.string.someday_maybe, R.id.nav_someday_maybe, View.VISIBLE, SomedayMaybeFilter),
    COMPLETED     (R.string.completed,     R.id.nav_completed,     View.GONE,    CompletedFilter),
    TRASH         (R.string.trash,         R.id.nav_trash,         View.GONE,    TrashFilter)
}

fun @receiver:IdRes Int.getTasksDisplayState(): TasksDisplayState {
    return values().find { it.navigationViewMenuId == this }
        ?: throw IllegalArgumentException("The resource ID must correspond to an ID from the navigation drawer menu!")
}
