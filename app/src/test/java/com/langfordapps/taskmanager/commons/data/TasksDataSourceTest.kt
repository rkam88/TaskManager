package com.langfordapps.taskmanager.commons.data

import com.langfordapps.taskmanager.commons.domain.BaseFilter
import com.langfordapps.taskmanager.commons.domain.model.Task
import com.langfordapps.taskmanager.commons.domain.model.TaskType
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.anyOrNull
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

class TasksDataSourceTest {

    private val taskDao: TaskDao = mock()
    private val mocks = arrayOf(taskDao)

    private val tasksDataSource = TasksDataSource(taskDao)

    @Test
    fun saveTask() = runBlocking {
        val taskToSave: Task = mock()
        val taskId = 10L
        whenever(taskDao.insertTask(any())).thenReturn(taskId)

        val result = tasksDataSource.saveTask(taskToSave)

        verify(taskDao).insertTask(taskToSave)
        verifyNoMoreInteractions(*mocks)
        assertEquals(taskId, result)
    }

    @Test
    fun getTasksCount() = runBlocking {
        val filter = BaseFilter.InboxFilter
        val expectedCount = 23L
        whenever(taskDao.getTasksCount(any(), any(), anyOrNull())).doReturn(expectedCount)

        val result = tasksDataSource.getTasksCount(filter)

        verify(taskDao).getTasksCount(filter.taskType, filter.hasDates != null, filter.hasDates)
        verifyNoMoreInteractions(*mocks)
        assertEquals(expectedCount, result)
    }

    @Test
    fun getTasks() = runBlocking {
        val filter = BaseFilter.InboxFilter
        val expected: List<Task> = mock()
        whenever(taskDao.getTasks(any(), any(), anyOrNull())).thenReturn(expected)

        val result = tasksDataSource.getTasks(filter)

        verify(taskDao).getTasks(filter.taskType, filter.hasDates != null, filter.hasDates)
        verifyNoMoreInteractions(*mocks)
        assertEquals(expected, result)
    }

    @Test
    fun getTaskById() = runBlocking {
        val taskId = 23L
        val expected: Task = mock()
        whenever(taskDao.getTask(taskId)).thenReturn(expected)

        val result = tasksDataSource.getTaskById(taskId)

        verify(taskDao).getTask(taskId)
        verifyNoMoreInteractions(*mocks)
        assertEquals(expected, result)
    }

    @Test
    fun markTaskAsCompleted() = runBlocking {
        val id = 23L

        tasksDataSource.markTaskAsCompleted(id)

        verify(taskDao).updateTaskType(id, TaskType.COMPLETED)
        verifyNoMoreInteractions(*mocks)
    }

    @Test
    fun deleteAllCompletedTasks() = runBlocking {
        tasksDataSource.deleteAllCompletedTasks()

        verify(taskDao).deleteTasks(TaskType.COMPLETED)
        verifyNoMoreInteractions(*mocks)
    }

    @Test
    fun deleteTasks() = runBlocking {
        val input: List<Long> = mock()

        tasksDataSource.deleteTasks(input)

        verify(taskDao).deleteTasks(input)
        verifyNoMoreInteractions(*mocks)
    }

    @Test
    fun getAllIncompleteTasks() = runBlocking {
        val expected: List<Task> = mock()
        whenever(taskDao.getTasksExcept(TaskType.COMPLETED)).thenReturn(expected)

        val result = tasksDataSource.getAllIncompleteTasks()

        verify(taskDao).getTasksExcept(TaskType.COMPLETED)
        verifyNoMoreInteractions(*mocks)
        assertEquals(expected, result)
    }

    @Test
    fun addTasks() = runBlocking {
        val input: List<Task> = mock()

        tasksDataSource.addTasks(input)

        verify(taskDao).insertTasks(input)
        verifyNoMoreInteractions(*mocks)
    }

}