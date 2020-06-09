package com.langfordapps.taskmanager.tasksdisplay.domain

import com.langfordapps.taskmanager.commons.data.TasksDataSource
import com.langfordapps.taskmanager.commons.domain.BaseFilter
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

class GetTasksCountUseCaseTest {

    private val baseFilter: BaseFilter = mock()
    private val tasksDataSource: TasksDataSource = mock()
    private val mocks = arrayOf(baseFilter, tasksDataSource)

    private val getTasksCountUseCase = GetTasksCountUseCase(tasksDataSource)

    @Test
    fun execute() = runBlocking {
        val fakeResult = 10L
        whenever(tasksDataSource.getTasksCount(baseFilter)).thenReturn(fakeResult)

        val result = getTasksCountUseCase.execute(baseFilter)

        verify(tasksDataSource).getTasksCount(baseFilter)
        verifyNoMoreInteractions(*mocks)
        assertEquals(fakeResult, result)
    }
}