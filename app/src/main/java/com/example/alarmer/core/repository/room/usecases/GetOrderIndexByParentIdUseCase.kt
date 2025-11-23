package com.example.alarmer.core.repository.room.usecases

import com.example.alarmer.core.repository.room.AlarmDao
import com.example.alarmer.core.repository.room.AlarmRepository
import javax.inject.Inject

class GetOrderIndexByParentIdUseCase @Inject constructor(
    private val alarmRepository: AlarmRepository
) {
    suspend operator fun invoke(parentId: Int): Int {
        return alarmRepository.getOrderIndexByParentId(parentId)
    }
}