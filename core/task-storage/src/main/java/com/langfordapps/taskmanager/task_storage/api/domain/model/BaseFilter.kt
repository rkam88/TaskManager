package com.langfordapps.taskmanager.task_storage.api.domain.model

sealed class BaseFilter(
    val status: TaskStatus,
    val hasDates: Boolean?,
) {
    //@formatter:off
    object Inbox:        BaseFilter(status = TaskStatus.INBOX,         hasDates = null)
    object NextActions:  BaseFilter(status = TaskStatus.ACTIVE,        hasDates = false)
    object Calendar:     BaseFilter(status = TaskStatus.ACTIVE,        hasDates = true)
    object WaitingFor:   BaseFilter(status = TaskStatus.WAITING_FOR,   hasDates = null)
    object SomedayMaybe: BaseFilter(status = TaskStatus.SOMEDAY_MAYBE, hasDates = null)
    object Completed:    BaseFilter(status = TaskStatus.COMPLETED,     hasDates = null)
    //@formatter:on
}