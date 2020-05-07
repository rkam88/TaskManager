package net.rusnet.taskmanager.tasksdisplay.presentation

import android.view.View
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import net.rusnet.taskmanager.R
import net.rusnet.taskmanager.tasksdisplay.presentation.TasksDisplayState.CALENDAR
import net.rusnet.taskmanager.tasksdisplay.presentation.TasksDisplayState.COMPLETED
import net.rusnet.taskmanager.tasksdisplay.presentation.TasksDisplayState.DELETED
import net.rusnet.taskmanager.tasksdisplay.presentation.TasksDisplayState.INBOX
import net.rusnet.taskmanager.tasksdisplay.presentation.TasksDisplayState.NEXT_ACTIONS
import net.rusnet.taskmanager.tasksdisplay.presentation.TasksDisplayState.SOMEDAY_MAYBE
import net.rusnet.taskmanager.tasksdisplay.presentation.TasksDisplayState.WAITING_FOR

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

////@ExperimentalStdlibApi
//val menuIdToTaskDisplayState = buildMap<Int, TasksDisplayState> {
//    for (state in TasksDisplayState.values()) {
//        put(state.menuId, state)
//    }
//}

// todo: replace with above code when buildMap() is no longer experimental
val menuIdToTaskDisplayState = mapOf(
    INBOX.navigationViewMenuId to INBOX,
    NEXT_ACTIONS.navigationViewMenuId to NEXT_ACTIONS,
    CALENDAR.navigationViewMenuId to CALENDAR,
    WAITING_FOR.navigationViewMenuId to WAITING_FOR,
    SOMEDAY_MAYBE.navigationViewMenuId to SOMEDAY_MAYBE,
    COMPLETED.navigationViewMenuId to COMPLETED,
    DELETED.navigationViewMenuId to DELETED
)

fun @receiver:IdRes Int.getTasksDisplayState(): TasksDisplayState {
    if (this !in menuIdToTaskDisplayState.keys) throw IllegalArgumentException("The resource ID must correspond to an ID from the navigation drawer menu!")
    return requireNotNull(menuIdToTaskDisplayState[this])
}
