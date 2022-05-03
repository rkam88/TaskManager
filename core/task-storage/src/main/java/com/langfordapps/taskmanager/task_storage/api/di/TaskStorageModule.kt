package com.langfordapps.taskmanager.task_storage.api.di

import android.content.Context
import androidx.room.Room
import com.langfordapps.taskmanager.task_storage.api.domain.TaskDataSource
import com.langfordapps.taskmanager.task_storage.internal.data.TaskDao
import com.langfordapps.taskmanager.task_storage.internal.data.TaskDataSourceImpl
import com.langfordapps.taskmanager.task_storage.internal.data.TasksDatabase
import org.koin.dsl.module

val taskStorageModule = module {

    single<TasksDatabase> {
        Room.databaseBuilder(
            get<Context>(),
            TasksDatabase::class.java,
            "tasks_database"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    single<TaskDao> {
        get<TasksDatabase>().taskDao
    }

    factory<TaskDataSource> {
        TaskDataSourceImpl(
            taskDao = get(),
        )
    }

}