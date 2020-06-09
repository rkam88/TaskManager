package com.langfordapps.taskmanager.edit.domain

import com.langfordapps.taskmanager.commons.data.TasksDataSource
import com.langfordapps.taskmanager.commons.domain.model.Task
import com.langfordapps.taskmanager.taskalarm.TaskAlarmService.TaskAlarmServiceHandler
import com.langfordapps.taskmanager.taskalarm.TaskAlarmServiceActions
import com.nhaarman.mockitokotlin2.inOrder
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.runBlocking
import org.junit.Test

class SaveTaskUseCaseTest {

    private val fakeTask: Task = mock()
    private val taskAlarmServiceHandler: TaskAlarmServiceHandler = mock()
    private val tasksDataSource: TasksDataSource = mock()
    private val mocks = arrayOf(fakeTask, taskAlarmServiceHandler, tasksDataSource)

    private val saveTaskUseCase = SaveTaskUseCase(taskAlarmServiceHandler, tasksDataSource)

    @Test
    fun execute() = runBlocking {
        val fakeId = 5L
        whenever(tasksDataSource.saveTask(fakeTask)).thenReturn(fakeId)

        saveTaskUseCase.execute(fakeTask)

        inOrder(*mocks) {
            verify(tasksDataSource).saveTask(fakeTask)
            verify(taskAlarmServiceHandler).enqueueWork(TaskAlarmServiceActions.UPDATE_ONE, fakeId)
        }
        verifyNoMoreInteractions(*mocks)
    }
}