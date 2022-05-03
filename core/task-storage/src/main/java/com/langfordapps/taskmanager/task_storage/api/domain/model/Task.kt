package com.langfordapps.taskmanager.task_storage.api.domain.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "task_table")
data class Task(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") val id: Long = 0,
    @ColumnInfo(name = "name") val name: String = "",
    @ColumnInfo(name = "status") val status: TaskStatus,
    @ColumnInfo(name = "details") val details: String? = null,
    @ColumnInfo(name = "start_date") val startDate: Long? = null,
    @ColumnInfo(name = "end_date") val endDate: Long? = null,
    @ColumnInfo(name = "alarm_date") val alarmDate: Long? = null,
) : Parcelable