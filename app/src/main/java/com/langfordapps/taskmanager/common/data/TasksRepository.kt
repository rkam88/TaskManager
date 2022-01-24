package com.langfordapps.taskmanager.common.data

import android.content.Context
import com.langfordapps.taskmanager.common.domain.Task

interface TasksRepository {
    fun getAllTasks(): List<Task>
    fun deleteTask(task: Task)
    fun markTaskAsComplete(task: Task)
    fun saveTask(task: Task)
}

class FakeTaskRepository(val context: Context) : TasksRepository {

    private var savedTasks = mutableListOf<Task>(
        Task(id = 1, name = "Task one", isCompleted = false),
        Task(id = 2, name = "Task two", isCompleted = false),
        Task(id = 3, name = "Task three", isCompleted = true),
        Task(id = 4, name = "Task four", isCompleted = false),
        Task(id = 5, name = "Task five", isCompleted = true),
        Task(id = 6, name = "Task siz", isCompleted = false),
        Task(id = 7, name = "Task seven", isCompleted = false),
    )

    override fun getAllTasks(): List<Task> {
        val uncomplete = savedTasks.filter { it.isCompleted.not() }.sortedBy { it.id }
        val complete = savedTasks.filter { it.isCompleted }.sortedBy { it.id }
        return uncomplete + complete
    }

    override fun deleteTask(task: Task) {
        savedTasks.remove(task)
    }

    override fun markTaskAsComplete(task: Task) {
        savedTasks = savedTasks.map {
            if (it == task) {
                it.copy(isCompleted = true)
            } else {
                it
            }
        }.toMutableList()
    }

    override fun saveTask(task: Task) {
        val taskToSave = if (task.id == 0L) {
            task.copy(id = getNewIndex())
        } else {
            task
        }
        savedTasks.add(taskToSave)
    }

    private fun getNewIndex(): Long {
        return (savedTasks.map { it.id }.maxByOrNull { it } ?: 0) + 1
    }

}