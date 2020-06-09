package com.langfordapps.taskmanager.taskalarm.domain

import com.langfordapps.taskmanager.commons.data.TasksDataSource
import com.langfordapps.taskmanager.commons.domain.model.Task
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

class GetAllIncompleteTasksUseCaseTest {

    private val fakeList: List<Task> = mock()
    private val tasksDataSource: TasksDataSource = mock()
    private val mocks = arrayOf(fakeList, tasksDataSource)

    private val getAllIncompleteTasksUseCase = GetAllIncompleteTasksUseCase(tasksDataSource)

    @Test
    fun execute() = runBlocking {
        whenever(tasksDataSource.getAllIncompleteTasks()).thenReturn(fakeList)

        val result = getAllIncompleteTasksUseCase.execute()

        verify(tasksDataSource).getAllIncompleteTasks()
        verifyNoMoreInteractions(*mocks)
        assertEquals(fakeList, result)
    }
}