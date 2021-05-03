package com.langfordapps.taskmanager.commons.di

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Resources
import androidx.room.Room
import com.langfordapps.taskmanager.R
import com.langfordapps.taskmanager.commons.data.TaskDao
import com.langfordapps.taskmanager.commons.data.TasksDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

// (object + @JvmStatic) is used to avoid creating
// instances of it because all methods will be static
@Module
@InstallIn(SingletonComponent::class)
object ApplicationModule {

    /**
     * Example for implementation to interface binding
     */
//    @Binds abstract fun provideSomeClass(someClassImpl: SomeClassImpl): SomeClassInterface

    @JvmStatic
    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences(
            context.getString(R.string.preferences_name_settings),
            Context.MODE_PRIVATE
        )
    }

    @JvmStatic
    @Provides
    @Singleton
    fun provideTasksDataBase(@ApplicationContext context: Context): TasksDatabase {
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

    @JvmStatic
    @Provides
    fun provideResources(@ApplicationContext context: Context): Resources {
        return context.resources
    }

    @IOCoroutineDispatcher
    @JvmStatic
    @Provides
    fun provideIoDispatcher(): CoroutineDispatcher {
        return Dispatchers.IO
    }

}

annotation class IOCoroutineDispatcher
