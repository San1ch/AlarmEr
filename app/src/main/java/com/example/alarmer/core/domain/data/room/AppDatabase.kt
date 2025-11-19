package com.example.alarmer.core.domain.data.room

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.TypeConverter
import androidx.room.Update
import com.example.alarmer.core.domain.data.alarm.AlarmAudio
import com.google.gson.Gson
import com.example.alarmer.core.domain.data.alarm.AlarmEntity
import com.example.alarmer.core.domain.data.alarm.AlarmTask
import com.example.alarmer.core.domain.data.alarm.DayOfWeek

@Database(
    entities = [AlarmEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(AlarmConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun alarmDao(): AlarmDao
}


