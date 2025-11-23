package com.example.alarmer.core.repository.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.alarmer.core.domain.data.alarm.AlarmEntity

@Database(
    entities = [AlarmEntity::class],
    version = 2,
    exportSchema = false
)
@TypeConverters(AlarmConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun alarmDao(): AlarmDao
}


