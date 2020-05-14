package net.rusnet.taskmanager.commons.domain

import net.rusnet.taskmanager.commons.domain.model.TaskType

sealed class BaseFilter(
        val isInTrash: Boolean,
        val isCompleted: Boolean?,
        val taskType: TaskType?,
        val hasDates: Boolean?
) {
    object InboxFilter :        BaseFilter(false, false, TaskType.INBOX,        null)
    object NextActionsFilter :  BaseFilter(false, false, TaskType.ACTIVE,       false)
    object CalendarFilter :     BaseFilter(false, false, TaskType.ACTIVE,       true)
    object WaitingForFilter :   BaseFilter(false, false, TaskType.WAITING_FOR,  null)
    object SomedayMaybeFilter : BaseFilter(false, false, TaskType.SOMEDAY_MAYBE,null)
    object CompletedFilter :    BaseFilter(false, true,  null,             null)
    object TrashFilter :        BaseFilter(true,  null,  null,             null)
}