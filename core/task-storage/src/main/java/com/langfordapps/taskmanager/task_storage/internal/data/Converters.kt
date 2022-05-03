package com.langfordapps.taskmanager.task_storage.internal.data

import androidx.room.TypeConverter
import com.langfordapps.taskmanager.task_storage.api.domain.model.TaskStatus

internal object Converters {

    @JvmStatic
    @TypeConverter
    fun fromTaskType(taskStatus: TaskStatus) = taskStatus.name

    @JvmStatic
    @TypeConverter
    fun toTaskType(taskStatusAsString: String): TaskStatus {
        return requireNotNull(TaskStatus.values().find { it.name == taskStatusAsString })
    }

}