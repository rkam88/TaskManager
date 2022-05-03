package com.langfordapps.taskmanager.tasklist.internal.presentation

import androidx.annotation.StringRes
import com.langfordapps.taskmanager.task_storage.api.domain.model.BaseFilter
import com.langfordapps.taskmanager.tasklist.R

internal sealed class TaskListType(
    @StringRes val titleRes: Int,
    val filter: BaseFilter,
) {
    object Inbox : TaskListType(R.string.inbox, BaseFilter.Inbox)
    object NextActions : TaskListType(R.string.next_actions, BaseFilter.NextActions)
    object Calendar : TaskListType(R.string.calendar, BaseFilter.Calendar)
    object WaitingFor : TaskListType(R.string.waiting_for, BaseFilter.WaitingFor)
    object SomedayMaybe : TaskListType(R.string.someday_maybe, BaseFilter.SomedayMaybe)
    object Completed : TaskListType(R.string.completed, BaseFilter.Completed)
}