package com.example.alarmer.core.domain.data.alarm

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo
import androidx.room.ForeignKey

/**
 * Stored alarm configuration.
 */
@Entity(
    tableName = "alarms",
    foreignKeys = [
        ForeignKey(
            entity = AlarmEntity::class,
            parentColumns = ["id"],
            childColumns = ["linked_to_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class AlarmEntity(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int = 0,

    @ColumnInfo(name = "order_index")
    val orderIndex: Int,

    @ColumnInfo(name = "time_minutes")
    val timeMinutes: Int,

    @ColumnInfo(name = "time_mode")
    val timeMode: TimeMode = TimeMode.STANDARD,

    @ColumnInfo(name = "linked_to_id", index = true)
    val linkedToId: Int? = null,

    @ColumnInfo(name = "is_alarm_ring_after")
    val isAlarmRingAfter: Boolean = false,

    @ColumnInfo(name = "offset_minutes")
    val offsetMinutes: Int = 0,

    @ColumnInfo(name = "is_enabled")
    val isEnabled: Boolean = true,

    @ColumnInfo(name = "label")
    val label: String = "",

    @ColumnInfo(name = "repeat_days")
    val repeatDays: List<DayOfWeek> = emptyList(),

    @ColumnInfo(name = "task")
    val task: AlarmTask? = null,

    @ColumnInfo(name = "disableHour")
    val disableHour: Int,

    @ColumnInfo(name = "disableMinute")
    val disableMinute: Int,

    @ColumnInfo(name = "audio")
    val audio: AlarmAudio,

    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis()
){
    fun getHour(): Int = timeMinutes / 60
    fun getMinute(): Int = timeMinutes % 60

    fun timeToMinutes(hour: Int, minutes: Int): Int = hour * 60 + minutes

    fun changeEnabled(isEnabled: Boolean): AlarmEntity {
        return copy(isEnabled = isEnabled)
    }
}



