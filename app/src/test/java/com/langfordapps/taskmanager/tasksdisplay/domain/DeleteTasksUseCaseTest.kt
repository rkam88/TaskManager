package com.langfordapps.taskmanager.tasksdisplay.domain

import com.langfordapps.taskmanager.commons.data.TasksDataSource
import com.langfordapps.taskmanager.taskalarm.TaskAlarmService
import com.langfordapps.taskmanager.taskalarm.TaskAlarmServiceActions
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import kotlinx.coroutines.runBlocking
import org.junit.Test

class DeleteTasksUseCaseTest {

    private val taskAlarmServiceHandler: TaskAlarmService.TaskAlarmServiceHandler = mock()
    private val tasksDataSource: TasksDataSource = mock()
    private val mocks = arrayOf(taskAlarmServiceHandler, tasksDataSource)

    private val deleteTasksUseCase = DeleteTasksUseCase(taskAlarmServiceHandler, tasksDataSource)

    @Test
    fun execute() = runBlocking {
        val fakeIdList = listOf(0L, 5L)

        deleteTasksUseCase.execute(fakeIdList)

        verify(tasksDataSource).deleteTasks(fakeIdList)
        fakeIdList.forEach { verify(taskAlarmServiceHandler).enqueueWork(TaskAlarmServiceActions.REMOVE_ONE, it) }
        verify(taskAlarmServiceHandler, times(fakeIdList.size)).enqueueWork(any(), any())
        verifyNoMoreInteractions(*mocks)
    }
}