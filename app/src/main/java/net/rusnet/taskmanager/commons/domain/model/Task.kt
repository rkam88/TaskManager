package net.rusnet.taskmanager.commons.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "task_table")
data class Task(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long = 0,

    @ColumnInfo(name = "name")
    val name: String = "",

    @ColumnInfo(name = "task_type")
    val taskType: TaskType,

    @ColumnInfo(name = "start_Date")
    val startDate: Long? = null,

    @ColumnInfo(name = "end_date")
    val endDate: Long? = null,

    @ColumnInfo(name = "is_completed")
    val isCompleted: Boolean = false,

    @ColumnInfo(name = "is_in_trash")
    val isInTrash: Boolean = false,

    @ColumnInfo(name = "reminder_date")
    val reminderDate: Long? = null

) : Serializable {
    companion object
}