package com.langfordapps.taskmanager.commons.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.langfordapps.taskmanager.commons.domain.model.Task
import com.langfordapps.taskmanager.commons.domain.model.TaskType

@Dao
interface TaskDao {

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

    @Query("SELECT * from task_table WHERE task_type != :excludeTaskType")
    suspend fun getTasksExcept(excludeTaskType: TaskType): List<Task>

    @Query(
        """
        SELECT *
        FROM task_table
        WHERE (task_type = :taskType)
        AND (:checkDates = 0 OR end_date IS NOT NULL = :hasDates)
        ORDER BY end_date IS NULL, start_date, end_date, id ASC
        """
    )
    suspend fun getTasks(
        taskType: TaskType,
        checkDates: Boolean,
        hasDates: Boolean?
    ): List<Task>

    @Query(
        """
        SELECT COUNT(*)
        FROM task_table
        WHERE (task_type = :taskType)
        AND (:checkDates = 0 OR end_date IS NOT NULL = :hasDates)
        """
    )
    suspend fun getTasksCount(
        taskType: TaskType,
        checkDates: Boolean,
        hasDates: Boolean?
    ): Long

    @Query("UPDATE task_table SET task_type = :newTaskType WHERE id = :taskId")
    suspend fun updateTaskType(taskId: Long, newTaskType: TaskType)

    @Query("DELETE FROM task_table WHERE task_type = :type")
    suspend fun deleteTasks(type: TaskType)

}