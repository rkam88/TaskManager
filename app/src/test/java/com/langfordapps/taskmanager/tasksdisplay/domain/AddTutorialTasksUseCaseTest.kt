package com.langfordapps.taskmanager.tasksdisplay.domain

import com.langfordapps.taskmanager.commons.data.TasksDataSource
import com.langfordapps.taskmanager.commons.domain.model.Task
import com.langfordapps.taskmanager.tasksdisplay.data.TutorialTasksProvider
import com.nhaarman.mockitokotlin2.inOrder
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.runBlocking
import org.junit.Test

class AddTutorialTasksUseCaseTest {

    private val fakeTutorialTasks: List<Task> = mock()
    private val tutorialTasksProvider: TutorialTasksProvider = mock()
    private val tasksDataSource: TasksDataSource = mock()
    private val mocks = arrayOf(fakeTutorialTasks, tutorialTasksProvider, tasksDataSource)

    private val addTutorialTasksUseCase = AddTutorialTasksUseCase(tutorialTasksProvider, tasksDataSource)

    @Test
    fun execute() = runBlocking {
        whenever(tutorialTasksProvider.getTutorialTasks()).thenReturn(fakeTutorialTasks)

        addTutorialTasksUseCase.execute()

        inOrder(*mocks) {
            verify(tutorialTasksProvider).getTutorialTasks()
            verify(tasksDataSource).addTasks(fakeTutorialTasks)
        }
        verifyNoMoreInteractions(*mocks)
    }
}