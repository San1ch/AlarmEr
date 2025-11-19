package com.example.alarmer.core.domain.ui.usecase.AlarmCreatorScreen

import androidx.lifecycle.ViewModel
import com.example.alarmer.core.domain.data.alarm.AlarmTaskType
import com.example.alarmer.core.domain.data.alarm.DayOfWeek
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class AlarmCreatorScreenViewModel @Inject constructor(

) : ViewModel() {
    private var _state = MutableStateFlow(AlarmCreatorScreenState())
    val state: StateFlow<AlarmCreatorScreenState> = _state.asStateFlow()

    fun onHourChanged(hour: String) {
        _state.value = _state.value.copy(hour = hour)
    }

    fun onMinuteChanged(minute: String) {
        _state.value = _state.value.copy(minute = minute)
    }

    fun onLabelChanged(label: String) {
        _state.value = _state.value.copy(label = label)
    }

    fun onRepeatDaysChanged(repeatDays: List<DayOfWeek>) {
        _state.value = _state.value.copy(repeatDays = repeatDays)
    }

    fun onDisableMillisChanged(disableMillis: String) {
        _state.value = _state.value.copy(disableMillis = disableMillis)
    }

    fun onSoundUriChanged(soundUri: String) {
        _state.value = _state.value.copy(soundUri = soundUri)
    }

    fun onVolumeChanged(volume: String) {
        _state.value = _state.value.copy(volume = volume)
    }

    fun onDayToggle(day: DayOfWeek) {
        _state.value = _state.value.copy(
            repeatDays = _state.value.repeatDays?.plus(day)
        )
    }

    //===Tasks


    fun onPhotoTask(){

    }

    fun onMathTask(){

    }
}

data class AlarmCreatorScreenState(
    val hour: String = "",
    val minute: String = "",
    val label: String = "",
    val repeatDays: List<DayOfWeek>? = null,
    val task: AlarmTaskType? = null,
    val disableMillis: String? = null,
    val soundUri: String? = null,
    val volume: String? = null
)