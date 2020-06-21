package com.langfordapps.taskmanager.tasksdisplay.data

import android.content.SharedPreferences
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.eq
import com.nhaarman.mockitokotlin2.inOrder
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SharedPreferencesManagerTest {

    private val dispatcher = TestCoroutineDispatcher()
    private val expectedResult = false
    private val sharedPreferences: SharedPreferences = mock()
    private val initialEditor: SharedPreferences.Editor = mock()
    private val modifiedEditor: SharedPreferences.Editor = mock()
    private val mocks = arrayOf(sharedPreferences, initialEditor, modifiedEditor)
    private val sharedPreferencesManager = SharedPreferencesManager(sharedPreferences, dispatcher)

    @Test
    fun isFirstLaunch() = dispatcher.runBlockingTest {
        whenever(sharedPreferences.getBoolean(eq(SharedPreferencesManager.KEY_IS_FIRST_LAUNCH), any())).doReturn(
            expectedResult
        )

        val actualResult = sharedPreferencesManager.isFirstLaunch()

        verify(sharedPreferences).getBoolean(SharedPreferencesManager.KEY_IS_FIRST_LAUNCH, true)
        verifyNoMoreInteractions(*mocks)
        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun setFirstLaunch() = dispatcher.runBlockingTest {
        val newValue = true
        whenever(sharedPreferences.edit()).thenReturn(initialEditor)
        whenever(
            initialEditor.putBoolean(
                SharedPreferencesManager.KEY_IS_FIRST_LAUNCH,
                newValue
            )
        ).thenReturn(modifiedEditor)

        sharedPreferencesManager.setFirstLaunch(newValue)

        inOrder(*mocks) {
            verify(sharedPreferences).edit()
            verify(initialEditor).putBoolean(SharedPreferencesManager.KEY_IS_FIRST_LAUNCH, newValue)
            verify(modifiedEditor).apply()
        }
        verifyNoMoreInteractions(*mocks)

    }
}