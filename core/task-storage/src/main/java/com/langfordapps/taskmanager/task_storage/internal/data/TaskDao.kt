package com.langfordapps.taskmanager.task_storage.internal.data

import androidx.room.*
import com.langfordapps.taskmanager.task_storage.api.domain.model.Task
import com.langfordapps.taskmanager.task_storage.api.domain.model.TaskStatus

@Dao
internal interface TaskDao {

    @Query("SELECT * FROM task_table WHERE id = :id")
    suspend fun getTask(id: Long): Task

    @Update
    suspend fun updateTask(task: Task)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: Task): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTasks(tasks: List<Task>)

    @Delete
    suspend fun deleteTask(task: Task)

    @Query("DELETE FROM task_table WHERE id IN (:taskIdList)")
    suspend fun deleteTasks(taskIdList: List<Long>)

    @Query("DELETE FROM task_table")
    suspend fun deleteAllTasks()

    @Query("SELECT * from task_table WHERE status != :excludeTaskStatus")
    suspend fun getTasksExcept(excludeTaskStatus: TaskStatus): List<Task>

    @Query(
        """
        SELECT *
        FROM task_table
        WHERE (status = :status)
        AND (:checkDates = 0 OR end_date IS NOT NULL = :hasDates)
        ORDER BY end_date IS NULL, start_date, end_date, id ASC
        """
    )
    suspend fun getTasks(
        status: TaskStatus,
        checkDates: Boolean,
        hasDates: Boolean?,
    ): List<Task>

    @Query(
        """
        SELECT COUNT(*)
        FROM task_table
        WHERE (status = :status)
        AND (:checkDates = 0 OR end_date IS NOT NULL = :hasDates)
        """
    )
    suspend fun getTasksCount(
        status: TaskStatus,
        checkDates: Boolean,
        hasDates: Boolean?,
    ): Long

    @Query("UPDATE task_table SET status = :newStatus WHERE id = :taskId")
    suspend fun updateTaskStatus(taskId: Long, newStatus: TaskStatus)

    @Query("DELETE FROM task_table WHERE status = :status")
    suspend fun deleteTasks(status: TaskStatus)

}