package com.langfordapps.taskmanager.common.domain

data class Task(
    val id: Long,
    val name: String,
    val isCompleted: Boolean,
)
