package com.example.alarmer.core.repository.room.usecases

import com.example.alarmer.core.domain.data.alarm.AlarmEntity
import com.example.alarmer.core.repository.room.AlarmRepository
import javax.inject.Inject


class GetAllAlarmsUseCase @Inject constructor(
    private val repository: AlarmRepository
) {
    suspend operator fun invoke(): List<AlarmEntity> {
        return repository.getAllAlarms()
    }
}