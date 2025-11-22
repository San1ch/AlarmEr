package com.example.alarmer.core.ui.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.alarmer.core.domain.data.alarm.AlarmTaskType
import com.example.alarmer.core.domain.data.alarm.DayOfWeek
import com.example.alarmer.core.domain.data.alarm.TimeMode
import com.example.alarmer.core.ui.usecase.AlarmCreatorScreen.CreateAlarmUseCase
import com.example.alarmer.core.ui.usecase.navigation.BackToPreviousScreenUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlarmCreatorScreenViewModel @Inject constructor(
    private val backToPreviousScreenUseCase: BackToPreviousScreenUseCase,
    private val createAlarmUseCase: CreateAlarmUseCase
) : ViewModel() {

    private val _stateUiContent = MutableStateFlow(AlarmCreatorScreenUiContentState())
    val stateUiContent: StateFlow<AlarmCreatorScreenUiContentState> = _stateUiContent.asStateFlow()



    // === TIME VALIDATION ===
    fun onHourChanged(hour: String) {
        val digits = hour.filter { it.isDigit() }.take(2)
        val value = digits.toIntOrNull()?.coerceIn(0, 23)?.toString()?.padStart(digits.length, '0')
        if (value == null) {
            _stateUiContent.value = _stateUiContent.value.copy(hour = "")
        } else {
            _stateUiContent.value = _stateUiContent.value.copy(hour = value)
        }
    }

    fun onMinuteChanged(minute: String) {
        val digits = minute.filter { it.isDigit() }.take(2)
        val value = digits.toIntOrNull()?.coerceIn(0, 59)?.toString()?.padStart(digits.length, '0')
        if (value == null) {
            _stateUiContent.value = _stateUiContent.value.copy(minute = "")
        } else {
            _stateUiContent.value = _stateUiContent.value.copy(minute = value)
        }
    }

    // === DISABLE MODE VALIDATION ===
    fun onDisableHourChanged(hour: String) {
        val digits = hour.filter { it.isDigit() }.take(2)
        val value = digits.toIntOrNull()?.coerceIn(0, 23)?.toString()?.padStart(digits.length, '0')
        if (value == null) {
            _stateUiContent.value = _stateUiContent.value.copy(disableHour = "")
        } else {
            _stateUiContent.value = _stateUiContent.value.copy(disableHour = value)
        }
    }

    fun onDisableMinuteChanged(minute: String) {
        val digits = minute.filter { it.isDigit() }.take(2)
        val value = digits.toIntOrNull()?.coerceIn(0, 59)?.toString()?.padStart(digits.length, '0')
        if (value == null) {
            _stateUiContent.value = _stateUiContent.value.copy(disableMinute = "")
        } else {
            _stateUiContent.value = _stateUiContent.value.copy(disableMinute = value)
        }
    }

    // === OTHER INPUTS ===
    fun onLabelChanged(label: String) {
        _stateUiContent.value = _stateUiContent.value.copy(label = label)
    }

    fun onRepeatDaysChanged(repeatDays: List<DayOfWeek>) {
        _stateUiContent.value = _stateUiContent.value.copy(repeatDays = repeatDays)
    }

    fun onTimeModeToggle() {
        val newMode = if (_stateUiContent.value.timeMode == TimeMode.STANDARD) {
            TimeMode.LINKED
        } else {
            TimeMode.STANDARD
        }
        _stateUiContent.value = _stateUiContent.value.copy(timeMode = newMode)
    }

    fun onEnabledDisableModeChanged(enabledDisableMode: Boolean) {
        _stateUiContent.value = _stateUiContent.value.copy(enabledDisableMode = enabledDisableMode)
    }

    fun onSoundUriChanged(soundUri: String) {
        _stateUiContent.value = _stateUiContent.value.copy(soundUri = soundUri)
    }

    fun onVolumeChanged(volume: Float) {
        _stateUiContent.value = _stateUiContent.value.copy(volume = volume)
    }

    fun onDayToggle(day: DayOfWeek) {
        val current = _stateUiContent.value.repeatDays
        val newList = if (current.contains(day)) {
            current - day
        } else {
            current + day
        }
        _stateUiContent.value = _stateUiContent.value.copy(repeatDays = newList)
    }

    fun extractFileName(uri: String): String {
        if (uri.isBlank()) return "No audio selected"
        return uri.substringAfterLast('/', "Unknown")
    }

    fun onSaveAlarm() {
        viewModelScope.launch{
            val result = createAlarmUseCase(stateUiContent.value)
            if(result){
                backToPreviousScreenUseCase()
            }
            else{
                println("Failed to create alarm")
            }
        }
    }

    // === TASK HANDLING ===
    fun onMathTask() {
        _stateUiContent.value = _stateUiContent.value.copy(task = AlarmTaskType.MATH)
    }

    fun onPhotoTask() {
        _stateUiContent.value = _stateUiContent.value.copy(
            photoTaskState = _stateUiContent.value.photoTaskState.copy(
                isDialogVisible = true
            )
        )
    }

    // === PHOTO DIALOG ===
    fun onPhotoDialogCancel() {
        _stateUiContent.value = _stateUiContent.value.copy(
            photoTaskState = _stateUiContent.value.photoTaskState.copy(
                isDialogVisible = false,
                selectedPhoto = null
            )
        )
    }

    fun onPhotoAdded(uri: String) {
        val current = _stateUiContent.value.photoTaskState
        _stateUiContent.value = _stateUiContent.value.copy(
            photoTaskState = current.copy(
                photos = current.photos + uri,
                selectedPhoto = uri
            )
        )
    }

    fun onPhotoThumbnailClick(uri: String) {
        val current = _stateUiContent.value.photoTaskState
        _stateUiContent.value = _stateUiContent.value.copy(
            photoTaskState = current.copy(selectedPhoto = uri)
        )
    }

    fun onPhotoDialogConfirm() {
        val current = _stateUiContent.value
        val selected = current.photoTaskState.selectedPhoto ?: return
        _stateUiContent.value = current.copy(
            task = AlarmTaskType.PHOTO,
            photoTaskState = current.photoTaskState.copy(isDialogVisible = false)
        )
    }

    fun onCancelAlarmCreatorClick() {
        backToPreviousScreenUseCase()
    }
}

// === STATE CLASSES ===
data class AlarmCreatorScreenUiContentState(
    val label: String = "",

    val timeMode: TimeMode = TimeMode.STANDARD,
    val hour: String = "",
    val minute: String = "",

    val repeatDays: List<DayOfWeek> = emptyList(),
    val task: AlarmTaskType? = null,

    val enabledDisableMode: Boolean = false,
    val disableHour: String = "",
    val disableMinute: String = "",

    val soundUri: String = "",
    val volume: Float = 0f,

    val photoTaskState: PhotoTaskState = PhotoTaskState()
)

data class PhotoTaskState(
    val isDialogVisible: Boolean = false,
    val photos: List<String> = emptyList(),
    val selectedPhoto: String? = null,
)
