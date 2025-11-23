package com.example.alarmer.core.repository.room.usecases

import com.example.alarmer.core.repository.room.AlarmRepository
import javax.inject.Inject

class EnableAlarmUseCase @Inject constructor(
    private val alarmRepository: AlarmRepository,
) {
    suspend operator fun invoke(id: Int, isEnabled: Boolean) {
        //TODO
        alarmRepository.changeAlarmEnabled(id, isEnabled)
    }
}