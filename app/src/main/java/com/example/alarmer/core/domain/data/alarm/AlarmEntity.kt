package com.example.alarmer.core.domain.data.alarm

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo

/**
 * Stored alarm configuration.
 */
@Entity(tableName = "alarms")
data class AlarmEntity(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int = 0,

    @ColumnInfo(name = "hour")
    val hour: Int,

    @ColumnInfo(name = "minute")
    val minute: Int,

    @ColumnInfo(name = "is_enabled")
    val isEnabled: Boolean = true,

    @ColumnInfo(name = "label")
    val label: String? = null,

    /**
     * Null → one-time alarm
     * Non-null → weekly repeating days
     */
    @ColumnInfo(name = "repeat_days")
    val repeatDays: List<DayOfWeek>? = null,

    /**
     * Null → no task
     * Non-null → task settings (photo, math…)
     */
    @ColumnInfo(name = "task")
    val task: AlarmTask? = null,

    /**
     * When >0 → alarm cannot be dismissed before triggerTime + this value
     */
    @ColumnInfo(name = "disable_before_ms")
    val disableBeforeMillis: Long? = null,

    @ColumnInfo(name = "audio")
    val audio: AlarmAudio,

    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis()
)



