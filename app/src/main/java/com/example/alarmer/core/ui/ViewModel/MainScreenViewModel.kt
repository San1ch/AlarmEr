package com.example.alarmer.core.ui.ViewModel

import androidx.lifecycle.ViewModel
import com.example.alarmer.core.repository.room.usecases.GetAllAlarmFlowUseCase
import com.example.alarmer.core.ui.navigation.NavigationTarget
import com.example.alarmer.core.ui.usecase.navigation.NavigateToScreenUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private val navigateToScreenUseCase: NavigateToScreenUseCase,
    private val getAlarmsUseCase: GetAllAlarmFlowUseCase
) : ViewModel() {

    private val _uiContentState = MutableStateFlow<List<MainScreenUiContentState>>(emptyList())
    val uiContentState: StateFlow<List<MainScreenUiContentState>> = _uiContentState.asStateFlow()

    val alarms = getAlarmsUseCase()

    fun onCreateAlarmClick() {
        navigateToScreenUseCase(NavigationTarget.AlarmCreatorScreens.Main)
    }

    fun onAlarmEnabledChange(id: Int, isEnabled: Boolean) {
        // тут потім зробиш use case для setEnabled()
    }

    fun onAlarmEditClick(id: Int) {
        // відкриття меню / навігація на екран редагування
    }
}


data class MainScreenUiContentState(
    val temp: String
)
