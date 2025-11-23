package com.example.alarmer.core.repository.room.usecases

import com.example.alarmer.core.domain.data.alarm.AlarmEntity
import com.example.alarmer.core.repository.room.AlarmRepository
import javax.inject.Inject

class ChangeAlarmEnabledUseCase @Inject constructor(
    private val alarmRepository: AlarmRepository,
) {
    suspend operator fun invoke(id: Int, isEnabled: Boolean) {
        alarmRepository.changeAlarmEnabled(id, isEnabled)
    }
}