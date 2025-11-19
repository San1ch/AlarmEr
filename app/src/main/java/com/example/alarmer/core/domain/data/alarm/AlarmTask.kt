package com.example.alarmer.core.domain.data.alarm

/**
 * Task that must be completed to dismiss alarm.
 */
data class AlarmTask(
    val type: AlarmTaskType? = null,
    val reference: String? = null,
    val taskTimerSec: Int? = null
)