package com.example.alarmer.core.repository.room.usecases

import com.example.alarmer.core.repository.room.AlarmRepository
import javax.inject.Inject

class DeleteAlarmUseCase @Inject constructor(
    private val alarmRepository: AlarmRepository
) {
    suspend operator fun invoke(id: Int) {
        alarmRepository.deleteAlarmById(id)
    }
}