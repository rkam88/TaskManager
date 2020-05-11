package net.rusnet.taskmanager.commons.domain

import net.rusnet.taskmanager.commons.domain.model.TaskType

sealed class BaseFilter(
    val isInTrash: Boolean,
    val isCompleted: Boolean?,
    val type: TaskType?,
    val isWaitingForTask: Boolean?,
    val hasDates: Boolean?
) {
    object InboxFilter :        BaseFilter(false, false, TaskType.INBOX,         null,  null)
    object NextActionsFilter :  BaseFilter(false, false, TaskType.ACTIVE,        false, false)
    object CalendarFilter :     BaseFilter(false, false, TaskType.ACTIVE,        false, true)
    object WaitingForFilter :   BaseFilter(false, false, TaskType.ACTIVE,        true,  null)
    object SomedayMaybeFilter : BaseFilter(false, false, TaskType.SOMEDAY_MAYBE, null,  null)
    object CompletedFilter :    BaseFilter(false, true, null,               null,  null)
    object TrashFilter :        BaseFilter(true,  null, null,               null,  null)
}