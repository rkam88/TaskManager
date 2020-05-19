package net.rusnet.taskmanager.commons.extensions

import net.rusnet.taskmanager.commons.domain.model.Task

fun Task.hasDates() = startDate != null && endDate != null

