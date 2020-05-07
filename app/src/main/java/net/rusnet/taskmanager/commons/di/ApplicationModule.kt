package net.rusnet.taskmanager.commons.di

import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import net.rusnet.taskmanager.R

// (object + @JvmStatic) is used to avoid creating
// instances of it because all methods will be static
@Module
object ApplicationModule {

    @JvmStatic
    @Provides
    fun provideSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(
            context.getString(R.string.preferences_name_settings),
            Context.MODE_PRIVATE
        )
    }

}