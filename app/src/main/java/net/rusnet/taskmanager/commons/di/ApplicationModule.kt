package net.rusnet.taskmanager.commons.di

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import dagger.Module
import dagger.Provides
import net.rusnet.taskmanager.R
import net.rusnet.taskmanager.commons.data.TaskDao
import net.rusnet.taskmanager.commons.data.TasksDatabase
import javax.inject.Singleton

// (object + @JvmStatic) is used to avoid creating
// instances of it because all methods will be static
@Module
object ApplicationModule {

    @JvmStatic
    @Provides
    @Singleton
    fun provideSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(
            context.getString(R.string.preferences_name_settings),
            Context.MODE_PRIVATE
        )
    }

    @JvmStatic
    @Provides
    @Singleton
    fun provideTasksDataBase(context: Context): TasksDatabase {
        return Room.databaseBuilder(
            context,
            TasksDatabase::class.java,
            "tasks_database"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @JvmStatic
    @Provides
    @Singleton
    fun provideTaskDao(database: TasksDatabase): TaskDao {
        return database.taskDao
    }

}