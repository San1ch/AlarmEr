package com.example.alarmer.core.repository.room.usecases

import com.example.alarmer.core.domain.data.alarm.AlarmEntity
import com.example.alarmer.core.repository.room.AlarmRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllAlarmFlowUseCase @Inject constructor(
    private val repository: AlarmRepository
) {
    operator fun invoke(): Flow<List<AlarmEntity>> {
        return repository.observeAllAlarms()
    }
}