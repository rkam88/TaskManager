package com.langfordapps.taskmanager.tasksdisplay.domain

import com.langfordapps.taskmanager.commons.data.TasksDataSource
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import kotlinx.coroutines.runBlocking
import org.junit.Test

class DeleteCompletedTasksUseCaseTest {

    private val tasksDataSource: TasksDataSource = mock()
    private val mocks = arrayOf(tasksDataSource)

    private val deleteCompletedTasksUseCase = DeleteCompletedTasksUseCase(tasksDataSource)

    @Test
    fun execute() = runBlocking {
        deleteCompletedTasksUseCase.execute()

        verify(tasksDataSource).deleteAllCompletedTasks()
        verifyNoMoreInteractions(*mocks)
    }
}