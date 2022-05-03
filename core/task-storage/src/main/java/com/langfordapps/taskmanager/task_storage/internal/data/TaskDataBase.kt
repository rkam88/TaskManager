package com.langfordapps.taskmanager.task_storage.internal.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.langfordapps.taskmanager.task_storage.api.domain.model.Task

@Database(entities = [Task::class], version = 1)
@TypeConverters(Converters::class)
internal abstract class TasksDatabase : RoomDatabase() {
    abstract val taskDao: TaskDao
}