package com.example.alarmer.core.domain.data.alarm


/**
 * Alarm sound & vibration settings.
 */
data class AlarmAudio(
    val soundUri: String,
    val volume: Float,
    val vibrationPattern: List<Long>? = null,
    val fadeInDurationSec: Int? = null
)
