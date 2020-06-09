package com.langfordapps.taskmanager.tasksdisplay.domain

import com.langfordapps.taskmanager.commons.data.TasksDataSource
import com.langfordapps.taskmanager.commons.domain.model.Task
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

class GetTaskByIdUseCaseTest {

    private val fakeTask: Task = mock()
    private val tasksDataSource: TasksDataSource = mock()
    private val mocks = arrayOf(fakeTask, tasksDataSource)

    private val getTaskByIdUseCase = GetTaskByIdUseCase(tasksDataSource)

    @Test
    fun execute() = runBlocking {
        val fakeId = 0L
        whenever(tasksDataSource.getTaskById(fakeId)).thenReturn(fakeTask)

        val result = getTaskByIdUseCase.execute(fakeId)

        verify(tasksDataSource).getTaskById(fakeId)
        verifyNoMoreInteractions(*mocks)
        assertEquals(fakeTask, result)
    }
}