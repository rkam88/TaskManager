package com.langfordapps.taskmanager.tasksdisplay.data

import android.content.SharedPreferences
import androidx.annotation.VisibleForTesting
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Named

class SharedPreferencesManager @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    @Named("IO") private val dispatcher: CoroutineDispatcher
) {

    @VisibleForTesting
    companion object {
        const val KEY_IS_FIRST_LAUNCH = "KEY_IS_FIRST_LAUNCH"
    }

    suspend fun isFirstLaunch(): Boolean {
        return withContext(dispatcher) {
            return@withContext sharedPreferences.getBoolean(KEY_IS_FIRST_LAUNCH, true)
        }
    }

    suspend fun setFirstLaunch(newValue: Boolean) {
        withContext(dispatcher) {
            sharedPreferences.edit().putBoolean(KEY_IS_FIRST_LAUNCH, newValue).apply()
        }
    }

}
