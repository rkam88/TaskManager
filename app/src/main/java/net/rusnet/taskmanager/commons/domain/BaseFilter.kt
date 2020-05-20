package net.rusnet.taskmanager.commons.domain

import net.rusnet.taskmanager.commons.domain.model.TaskType

sealed class BaseFilter(
        val taskType: TaskType?,
        val hasDates: Boolean?
) {
    object InboxFilter :        BaseFilter(taskType = TaskType.INBOX,         hasDates = null)
    object NextActionsFilter :  BaseFilter(taskType = TaskType.ACTIVE,        hasDates = false)
    object CalendarFilter :     BaseFilter(taskType = TaskType.ACTIVE,        hasDates = true)
    object WaitingForFilter :   BaseFilter(taskType = TaskType.WAITING_FOR,   hasDates = null)
    object SomedayMaybeFilter : BaseFilter(taskType = TaskType.SOMEDAY_MAYBE, hasDates = null)
    object CompletedFilter :    BaseFilter(taskType = TaskType.COMPLETED,     hasDates = null)
    object TrashFilter :        BaseFilter(taskType = TaskType.TRASH,         hasDates = null)
}