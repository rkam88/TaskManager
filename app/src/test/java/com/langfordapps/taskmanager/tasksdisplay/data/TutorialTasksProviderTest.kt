package com.langfordapps.taskmanager.tasksdisplay.data

import android.content.res.Resources
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Assert.assertEquals
import org.junit.Test

class TutorialTasksProviderTest {

    private val taskNamesArray = Array(10) { i -> "Task number #$i" }
    private val resources: Resources = mock()
    private val mocks = arrayOf(resources)
    private val tutorialTasksProvider = TutorialTasksProvider(resources)

    @Test
    fun getTutorialTasks() {
        whenever(resources.getStringArray(any())).thenReturn(taskNamesArray)

        val generatedTutorialTasks = tutorialTasksProvider.getTutorialTasks()

        verify(resources).getStringArray(any())
        verifyNoMoreInteractions(*mocks)

        assertEquals(taskNamesArray.size, generatedTutorialTasks.size)
        for ((index, taskName) in taskNamesArray.withIndex()) {
            assertEquals(taskName, generatedTutorialTasks[index].name)
        }
    }
}