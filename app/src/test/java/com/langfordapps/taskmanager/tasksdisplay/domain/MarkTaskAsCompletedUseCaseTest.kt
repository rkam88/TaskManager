package com.langfordapps.taskmanager.tasksdisplay.domain

import com.langfordapps.taskmanager.commons.data.TasksDataSource
import com.langfordapps.taskmanager.taskalarm.TaskAlarmService.TaskAlarmServiceHandler
import com.langfordapps.taskmanager.taskalarm.TaskAlarmServiceActions
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import kotlinx.coroutines.runBlocking
import org.junit.Test

class MarkTaskAsCompletedUseCaseTest {

    private val taskAlarmServiceHandler: TaskAlarmServiceHandler = mock()
    private val tasksDataSource: TasksDataSource = mock()
    private val mocks = arrayOf(taskAlarmServiceHandler, tasksDataSource)

    private val markTaskAsCompletedUseCase = MarkTaskAsCompletedUseCase(taskAlarmServiceHandler, tasksDataSource)

    @Test
    fun execute() = runBlocking {
        val fakeId = 0L

        markTaskAsCompletedUseCase.execute(fakeId)

        verify(tasksDataSource).markTaskAsCompleted(fakeId)
        verify(taskAlarmServiceHandler).enqueueWork(TaskAlarmServiceActions.REMOVE_ONE, fakeId)
        verifyNoMoreInteractions(*mocks)
    }
}