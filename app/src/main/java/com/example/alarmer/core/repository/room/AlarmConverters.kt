package com.example.alarmer.core.repository.room

import androidx.room.TypeConverter
import com.example.alarmer.core.domain.data.alarm.AlarmAudio
import com.example.alarmer.core.domain.data.alarm.AlarmTask
import com.example.alarmer.core.domain.data.alarm.DayOfWeek
import com.example.alarmer.core.domain.data.alarm.TimeMode
import com.google.gson.Gson


class AlarmConverters {

    private val gson = Gson()

    @TypeConverter
    fun fromDayList(value: List<DayOfWeek>?): String? =
        value?.joinToString(",")

    @TypeConverter
    fun toDayList(value: String?): List<DayOfWeek>? =
        value?.split(",")?.map { DayOfWeek.valueOf(it) }

    @TypeConverter
    fun fromTask(value: AlarmTask?): String? =
        if (value == null) null else gson.toJson(value)

    @TypeConverter
    fun toTask(value: String?): AlarmTask? =
        if (value == null) null
        else gson.fromJson(value, AlarmTask::class.java)

    @TypeConverter
    fun fromAudio(value: AlarmAudio?): String? =
        if (value == null) null else gson.toJson(value)

    @TypeConverter
    fun toAudio(value: String?): AlarmAudio? =
        if (value == null) null
        else gson.fromJson(value, AlarmAudio::class.java)

    @TypeConverter
    fun fromTimeMode(value: TimeMode): Int =
        when(value){
            TimeMode.STANDARD -> 1
            TimeMode.LINKED -> 2
        }

    @TypeConverter
    fun toTimeMode(value: Int): TimeMode =
        when(value){
            1 -> TimeMode.STANDARD
            2 -> TimeMode.LINKED
            else -> TimeMode.STANDARD
        }

}