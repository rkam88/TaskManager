package net.rusnet.taskmanager.commons.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import net.rusnet.taskmanager_old.commons.domain.model.Task

@Database(entities = [Task::class], version = 1)
@TypeConverters(Converters::class)
abstract class TasksDatabase : RoomDatabase() {

    abstract val taskDao: TaskDao

}