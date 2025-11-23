package com.example.alarmer.core.repository.room.di

import android.content.Context
import androidx.room.Room
import com.example.alarmer.core.repository.room.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        return Room.databaseBuilder(
                context,
                AppDatabase::class.java,
                "alarmer.db"
            ).fallbackToDestructiveMigration(false).build()
    }

    @Provides
    fun provideAlarmDao(db: AppDatabase) = db.alarmDao()
}