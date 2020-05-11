package net.rusnet.taskmanager.commons.data

import androidx.room.*
import net.rusnet.taskmanager.commons.domain.model.TaskType
import net.rusnet.taskmanager_old.commons.domain.model.Task

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
                "AND (:type IS NULL OR type = :type) " +
                "AND (:isWaitingForTask IS NULL OR is_waiting_for_task = :isWaitingForTask) " +
                "AND (end_date IS NOT NULL = :hasDates) " +
                "ORDER BY end_date IS NULL, end_date, start_date, id ASC"
    )
    suspend fun getTasks(
        isInTrash: Boolean,
        isCompleted: Boolean?,
        type: TaskType?,
        isWaitingForTask: Boolean?,
        hasDates: Boolean?
    ): List<Task>

    @Query(
        "SELECT COUNT(*) " +
                "FROM task_table " +
                "WHERE (is_in_trash = :isInTrash) " +
                "AND (:isCompleted IS NULL OR is_completed = :isCompleted) " +
                "AND (:type IS NULL OR type = :type) " +
                "AND (:isWaitingForTask IS NULL OR is_waiting_for_task = :isWaitingForTask) " +
                "AND (end_date IS NOT NULL = :hasDates)"
    )
    suspend fun getTasksCount(
        isInTrash: Boolean,
        isCompleted: Boolean?,
        type: TaskType?,
        isWaitingForTask: Boolean?,
        hasDates: Boolean?
    ): Long

}