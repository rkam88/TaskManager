package net.rusnet.taskmanager.commons.domain

import net.rusnet.taskmanager.commons.domain.model.TaskType

sealed class BaseFilter(
        val isInTrash: Boolean,
        val isCompleted: Boolean?,
        val taskType: TaskType?,
        val hasDates: Boolean?
) {
    object InboxFilter :        BaseFilter(isInTrash = false, isCompleted = false, taskType = TaskType.INBOX,         hasDates = null)
    object NextActionsFilter :  BaseFilter(isInTrash = false, isCompleted = false, taskType = TaskType.ACTIVE,        hasDates = false)
    object CalendarFilter :     BaseFilter(isInTrash = false, isCompleted = false, taskType = TaskType.ACTIVE,        hasDates = true)
    object WaitingForFilter :   BaseFilter(isInTrash = false, isCompleted = false, taskType = TaskType.WAITING_FOR,   hasDates = null)
    object SomedayMaybeFilter : BaseFilter(isInTrash = false, isCompleted = false, taskType = TaskType.SOMEDAY_MAYBE, hasDates = null)
    object CompletedFilter :    BaseFilter(isInTrash = false, isCompleted = true,  taskType = null,                   hasDates = null)
    object TrashFilter :        BaseFilter(isInTrash = true,  isCompleted = null,  taskType = null,                   hasDates = null)
}