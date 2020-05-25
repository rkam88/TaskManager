package com.langfordapps.taskmanager.tasksdisplay.data

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

private const val PREFERENCES_NAME = "Settings"
private const val KEY_IS_FIRST_LAUNCH = "KEY_IS_FIRST_LAUNCH"

class SharedPreferencesManager @Inject constructor(context: Context) {

    private val sharedPreferences = context.applicationContext.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)

    suspend fun isFirstLaunch(): Boolean {
        return withContext(Dispatchers.IO) {
            return@withContext sharedPreferences.getBoolean(KEY_IS_FIRST_LAUNCH, true)
        }
    }

    suspend fun setFirstLaunch(newValue: Boolean) {
        withContext(Dispatchers.IO) {
            sharedPreferences.edit().putBoolean(KEY_IS_FIRST_LAUNCH, newValue).apply()
        }
    }

}
