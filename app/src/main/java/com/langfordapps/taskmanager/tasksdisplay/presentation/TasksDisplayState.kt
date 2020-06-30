package com.langfordapps.taskmanager.tasksdisplay.presentation

import androidx.annotation.IdRes
import androidx.annotation.MenuRes
import androidx.annotation.StringRes
import com.langfordapps.taskmanager.R
import com.langfordapps.taskmanager.commons.domain.BaseFilter
import com.langfordapps.taskmanager.commons.domain.model.TaskType
import com.langfordapps.taskmanager.commons.presentation.ViewVisibility
import com.langfordapps.taskmanager.commons.presentation.ViewVisibility.GONE
import com.langfordapps.taskmanager.commons.presentation.ViewVisibility.VISIBLE

sealed class TasksDisplayState(
    @StringRes val toolbarTitle: Int,
    @IdRes val navigationViewMenuId: Int,
    val addButtonVisibility: ViewVisibility,
    val baseFilter: BaseFilter,
    val newTaskType: TaskType,
    val isSwipeEnabled: Boolean,
    @MenuRes val toolbarMenu: Int?,
    val isActionModeEnabled: Boolean = false,
    val actionModeTitle: String = ""
) {
    object Inbox :
        TasksDisplayState(R.string.inbox, R.id.nav_inbox, VISIBLE, BaseFilter.InboxFilter, TaskType.INBOX, true, null)

    object NextActions : TasksDisplayState(
        R.string.next_actions,
        R.id.nav_next_actions,
        VISIBLE,
        BaseFilter.NextActionsFilter,
        TaskType.ACTIVE,
        true,
        null
    )

    object Calendar : TasksDisplayState(
        R.string.calendar,
        R.id.nav_calendar,
        VISIBLE,
        BaseFilter.CalendarFilter,
        TaskType.ACTIVE,
        true,
        null
    )

    object WaitingFor : TasksDisplayState(
        R.string.waiting_for,
        R.id.nav_waiting_for,
        VISIBLE,
        BaseFilter.WaitingForFilter,
        TaskType.WAITING_FOR,
        true,
        null
    )

    object SomedayMaybe : TasksDisplayState(
        R.string.someday_maybe,
        R.id.nav_someday_maybe,
        VISIBLE,
        BaseFilter.SomedayMaybeFilter,
        TaskType.SOMEDAY_MAYBE,
        true,
        null
    )

    object Completed : TasksDisplayState(
        R.string.completed,
        R.id.nav_completed,
        GONE,
        BaseFilter.CompletedFilter,
        TaskType.INBOX,
        false,
        R.menu.tasks_display_completed_menu
    )

    class Custom(
        private val baseState: TasksDisplayState,
        newToolbarTitle: Int = baseState.toolbarTitle,
        newNavigationViewMenuId: Int = baseState.navigationViewMenuId,
        newAddButtonVisibility: ViewVisibility = baseState.addButtonVisibility,
        newBaseFilter: BaseFilter = baseState.baseFilter,
        newNewTaskType: TaskType = baseState.newTaskType,
        newIsSwipeEnabled: Boolean = baseState.isSwipeEnabled,
        newToolbarMenu: Int? = baseState.toolbarMenu,
        newIsActionModeEnabled: Boolean = baseState.isActionModeEnabled,
        newActionModeTitle: String = baseState.actionModeTitle
    ) : TasksDisplayState(
        toolbarTitle = newToolbarTitle,
        navigationViewMenuId = newNavigationViewMenuId,
        addButtonVisibility = newAddButtonVisibility,
        baseFilter = newBaseFilter,
        newTaskType = newNewTaskType,
        isSwipeEnabled = newIsSwipeEnabled,
        toolbarMenu = newToolbarMenu,
        isActionModeEnabled = newIsActionModeEnabled,
        actionModeTitle = newActionModeTitle
    )

}

val BASE_TASK_DISPLAY_STATES = listOf(
    TasksDisplayState.Inbox,
    TasksDisplayState.NextActions,
    TasksDisplayState.Calendar,
    TasksDisplayState.WaitingFor,
    TasksDisplayState.SomedayMaybe,
    TasksDisplayState.Completed
)

fun @receiver:IdRes Int.getTasksDisplayState(): TasksDisplayState {
    return BASE_TASK_DISPLAY_STATES.find { it.navigationViewMenuId == this }
        ?: throw IllegalArgumentException("The resource ID must correspond to an ID from the navigation drawer menu!")
}