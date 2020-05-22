package com.langfordapps.taskmanager.commons.data

import androidx.room.TypeConverter
import com.langfordapps.taskmanager.commons.domain.model.TaskType

object Converters {

    @JvmStatic
    @TypeConverter
    fun fromTaskType(taskType: TaskType?) = taskType?.name

    @JvmStatic
    @TypeConverter
    fun toTaskType(taskTypeAsString: String): TaskType {
        return requireNotNull(TaskType.values().find { it.name == taskTypeAsString })
    }

}