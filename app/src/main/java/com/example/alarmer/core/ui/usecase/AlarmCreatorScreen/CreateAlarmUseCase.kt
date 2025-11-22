package com.example.alarmer.core.ui.usecase.AlarmCreatorScreen

import com.example.alarmer.core.domain.data.alarm.AlarmAudio
import com.example.alarmer.core.domain.data.alarm.AlarmEntity
import com.example.alarmer.core.domain.data.alarm.AlarmTask
import com.example.alarmer.core.domain.data.alarm.AlarmTaskType
import com.example.alarmer.core.repository.room.AlarmRepository
import com.example.alarmer.core.ui.ViewModel.AlarmCreatorScreenUiContentState
import javax.inject.Inject

class CreateAlarmUseCase @Inject constructor(
    private val alarmRepository: AlarmRepository,
) {

    suspend operator fun invoke(state: AlarmCreatorScreenUiContentState): Boolean {
        if (!validate(state)) return false

        val maxOrder = alarmRepository.getMaxOrderIndex()
        val newOrderIndex = maxOrder + 1

        val hour = state.hour.toInt()
        val minute = state.minute.toInt()

        val disableHour = if (state.enabledDisableMode) {
            state.disableHour.toInt()
        } else {
            0
        }

        val disableMinute = if (state.enabledDisableMode) {
            state.disableMinute.toInt()
        } else {
            0
        }

        val alarmTask: AlarmTask? = when (state.task) {
            AlarmTaskType.PHOTO -> {
                AlarmTask(
                    type = state.task,
                    reference = state.photoTaskState.selectedPhoto,
                    possiblesReferences = state.photoTaskState.photos,
                    taskTimerSec = 30, // TODO: make this parameter configurable in AlarmCreatorScreen
                )
            }

            AlarmTaskType.MATH -> {
                null //TODO
            }

            null -> null
        }

        val audio = AlarmAudio(
            soundUri = state.soundUri,
            volume = state.volume.coerceIn(0f, 1f)
        )

        val alarm = AlarmEntity(
            id = 0,
            orderIndex = newOrderIndex,
            hour = hour,
            minute = minute,
            isEnabled = false,
            label = state.label.trim(),
            repeatDays = state.repeatDays,
            task = alarmTask,
            disableHour = disableHour,
            disableMinute = disableMinute,
            audio = audio,
            createdAt = System.currentTimeMillis()
        )

        alarmRepository.insertAlarm(alarm)
        return true
    }

    private fun validate(state: AlarmCreatorScreenUiContentState): Boolean {
        val label = state.label.trim()
        if (label.isEmpty()) return false

        val hour = state.hour.toIntOrNull() ?: return false
        val minute = state.minute.toIntOrNull() ?: return false
        if (hour !in 0..23 || minute !in 0..59) return false

        if (state.enabledDisableMode) {
            val dh = state.disableHour.toIntOrNull() ?: return false
            val dm = state.disableMinute.toIntOrNull() ?: return false
            if (dh !in 0..23 || dm !in 0..59) return false
        }

        if (state.volume !in 0f..1f) return false
        if (state.soundUri.isBlank()) return false

        when (state.task) {
            AlarmTaskType.PHOTO -> {
                val photos = state.photoTaskState.photos
                if (photos.isEmpty() || state.photoTaskState.selectedPhoto == null) return false
            }

            AlarmTaskType.MATH -> return false
            null -> {}
        }

        return true
    }
}

enum class CreateAlarmResult {
    Success,
    Error
}
