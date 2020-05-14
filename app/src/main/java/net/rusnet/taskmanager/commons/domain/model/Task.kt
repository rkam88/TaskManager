package net.rusnet.taskmanager_old.commons.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import net.rusnet.taskmanager.commons.domain.model.TaskType
import java.io.Serializable
import java.util.Date

@Entity(tableName = "task_table")
data class Task(

        @PrimaryKey(autoGenerate = true)
        @ColumnInfo(name = "id")
        val id: Long = 0,

        @ColumnInfo(name = "name")
        val name: String = "",

        @ColumnInfo(name = "type")
        val type: TaskType,

        @ColumnInfo(name = "start_Date")
        val startDate: Date? = null,

        @ColumnInfo(name = "end_date")
        val endDate: Date? = null,

        @ColumnInfo(name = "is_completed")
        val isCompleted: Boolean = false,

        @ColumnInfo(name = "is_in_trash")
        val isInTrash: Boolean = false,

        @ColumnInfo(name = "reminder_date")
        val reminderDate: Date? = null

) : Serializable