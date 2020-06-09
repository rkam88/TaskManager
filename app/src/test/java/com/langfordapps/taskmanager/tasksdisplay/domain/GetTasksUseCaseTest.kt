package com.langfordapps.taskmanager.tasksdisplay.domain

import com.langfordapps.taskmanager.commons.data.TasksDataSource
import com.langfordapps.taskmanager.commons.domain.BaseFilter
import com.langfordapps.taskmanager.commons.domain.model.Task
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

class GetTasksUseCaseTest {

    private val fakeTasks: List<Task> = mock()
    private val baseFilter: BaseFilter = mock()
    private val tasksDataSource: TasksDataSource = mock()
    private val mocks = arrayOf(fakeTasks, baseFilter, tasksDataSource)

    private val getTasksUseCase = GetTasksUseCase(tasksDataSource)

    @Test
    fun execute() = runBlocking {
        whenever(tasksDataSource.getTasks(baseFilter)).thenReturn(fakeTasks)

        val result = getTasksUseCase.execute(baseFilter)

        verify(tasksDataSource).getTasks(baseFilter)
        verifyNoMoreInteractions(*mocks)
        assertEquals(fakeTasks, result)
    }
}