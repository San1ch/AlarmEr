package com.example.alarmer.core.repository.room.usecases

import com.example.alarmer.core.domain.data.alarm.AlarmEntity
import com.example.alarmer.core.repository.room.AlarmRepository
import javax.inject.Inject

class GetAlarmByIdUseCase @Inject constructor(
    private val repository: AlarmRepository
) {
    suspend operator fun invoke(id: Int): AlarmEntity? {
        return repository.getAlarmById(id)
    }
}