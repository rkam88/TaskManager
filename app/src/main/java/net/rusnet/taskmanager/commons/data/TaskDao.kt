package net.rusnet.taskmanager.commons.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import net.rusnet.taskmanager.commons.domain.model.TaskType
import net.rusnet.taskmanager.commons.domain.model.Task

@Dao
interface TaskDao {

    @Query("SELECT * FROM task_table WHERE id = :id")
    suspend fun getTask(id: Long): Task

    @Update
    suspend fun updateTask(task: Task)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: Task)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTasks(tasks: List<Task>)

    @Delete
    suspend fun deleteTask(task: Task)

    @Delete
    suspend fun deleteTasks(tasks: List<Task>)

    @Query("DELETE FROM task_table")
    suspend fun deleteAllTasks()

    @Query("SELECT * from task_table")
    suspend fun getAllTasks(): List<Task>

    @Query(
        "SELECT * " +
                "FROM task_table " +
                "WHERE (is_in_trash = :isInTrash) " +
                "AND (:isCompleted IS NULL OR is_completed = :isCompleted) " +
                "AND (:taskType IS NULL OR task_type = :taskType) " +
                "AND (end_date IS NOT NULL = :hasDates) " +
                "ORDER BY end_date IS NULL, end_date, start_date, id ASC"
    )
    suspend fun getTasks(
            isInTrash: Boolean,
            isCompleted: Boolean?,
            taskType: TaskType?,
            hasDates: Boolean?
    ): List<Task>

    @Query(
        "SELECT COUNT(*) " +
                "FROM task_table " +
                "WHERE (is_in_trash = :isInTrash) " +
                "AND (:isCompleted IS NULL OR is_completed = :isCompleted) " +
                "AND (:taskType IS NULL OR task_type = :taskType) " +
                "AND (end_date IS NOT NULL = :hasDates)"
    )
    suspend fun getTasksCount(
            isInTrash: Boolean,
            isCompleted: Boolean?,
            taskType: TaskType?,
            hasDates: Boolean?
    ): Long

}