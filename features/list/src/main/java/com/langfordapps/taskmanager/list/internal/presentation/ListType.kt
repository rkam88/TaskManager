package com.langfordapps.taskmanager.list.internal.presentation

import androidx.annotation.StringRes
import com.langfordapps.taskmanager.list.R
import com.langfordapps.taskmanager.task_storage.api.domain.model.BaseFilter

internal sealed class ListType(
    @StringRes val titleRes: Int,
    val filter: BaseFilter,
) {
    object Inbox : ListType(R.string.inbox, BaseFilter.Inbox)
    object NextActions : ListType(R.string.next_actions, BaseFilter.NextActions)
    object Calendar : ListType(R.string.calendar, BaseFilter.Calendar)
    object WaitingFor : ListType(R.string.waiting_for, BaseFilter.WaitingFor)
    object SomedayMaybe : ListType(R.string.someday_maybe, BaseFilter.SomedayMaybe)
    object Completed : ListType(R.string.completed, BaseFilter.Completed)
}