package net.rusnet.taskmanager.tasksdisplay.presentation

import android.view.View
import androidx.annotation.IdRes
import androidx.annotation.MenuRes
import androidx.annotation.StringRes
import net.rusnet.taskmanager.R
import net.rusnet.taskmanager.commons.domain.BaseFilter
import net.rusnet.taskmanager.commons.domain.BaseFilter.*
import net.rusnet.taskmanager.commons.domain.model.TaskType
import net.rusnet.taskmanager.tasksdisplay.presentation.TasksDisplayState.*

enum class TasksDisplayState(
    @StringRes val toolbarTitle: Int,
    @IdRes val navigationViewMenuId: Int,
    val addButtonVisibility: Int,
    val baseFilter: BaseFilter,
    val newTaskType: TaskType,
    val isSwipeEnabled: Boolean,
    @MenuRes val toolbarMenu: Int?
) {
    INBOX         (R.string.inbox,         R.id.nav_inbox,         View.VISIBLE, InboxFilter,        TaskType.INBOX         ,true,  null),
    NEXT_ACTIONS  (R.string.next_actions,  R.id.nav_next_actions,  View.VISIBLE, NextActionsFilter,  TaskType.ACTIVE        ,true,  null),
    CALENDAR      (R.string.calendar,      R.id.nav_calendar,      View.VISIBLE, CalendarFilter,     TaskType.ACTIVE        ,true,  null),
    WAITING_FOR   (R.string.waiting_for,   R.id.nav_waiting_for,   View.VISIBLE, WaitingForFilter,   TaskType.WAITING_FOR   ,true,  null),
    SOMEDAY_MAYBE (R.string.someday_maybe, R.id.nav_someday_maybe, View.VISIBLE, SomedayMaybeFilter, TaskType.SOMEDAY_MAYBE ,true,  null),
    COMPLETED     (R.string.completed,     R.id.nav_completed,     View.GONE,    CompletedFilter,    TaskType.INBOX         ,false, R.menu.taks_display_completed_menu),
    TRASH         (R.string.trash,         R.id.nav_trash,         View.GONE,    TrashFilter,        TaskType.INBOX         ,false, null)
}

fun @receiver:IdRes Int.getTasksDisplayState(): TasksDisplayState {
    return values().find { it.navigationViewMenuId == this }
        ?: throw IllegalArgumentException("The resource ID must correspond to an ID from the navigation drawer menu!")
}
