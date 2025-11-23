package com.example.alarmer.core.ui.ViewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.alarmer.core.domain.data.alarm.AlarmEntity
import com.example.alarmer.core.repository.room.usecases.ChangeAlarmEnabledUseCase
import com.example.alarmer.core.repository.room.usecases.DeleteAlarmUseCase
import com.example.alarmer.core.repository.room.usecases.EnableAlarmUseCase
import com.example.alarmer.core.repository.room.usecases.GetAllAlarmFlowsUseCase
import com.example.alarmer.core.repository.room.usecases.GetOrderIndexByParentIdUseCase
import com.example.alarmer.core.ui.navigation.NavigationTarget
import com.example.alarmer.core.ui.usecase.navigation.NavigateToScreenUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private val navigateToScreenUseCase: NavigateToScreenUseCase,
    private val getAllAlarmFlowsUseCase: GetAllAlarmFlowsUseCase,
    private val getOrderIndexByParentIdUseCase: GetOrderIndexByParentIdUseCase,
    private val enableAlarmUseCase: EnableAlarmUseCase,
    private val deleteAlarmUseCase: DeleteAlarmUseCase,
) : ViewModel() {

    val alarms = getAllAlarmFlowsUseCase()
    private val _uiContentState = MutableStateFlow<List<MainScreenUiContentState>>(emptyList())
    val uiContentState: StateFlow<List<MainScreenUiContentState>> = _uiContentState.asStateFlow()



    fun onCreateAlarmClick() {
        navigateToScreenUseCase(NavigationTarget.AlarmCreatorScreens.Main)
    }

    fun onAlarmEnabledChange(id: Int, isEnabled: Boolean) {
        viewModelScope.launch {
            enableAlarmUseCase(id, isEnabled)
        }
    }

    fun onAlarmEditClick(id: Int) {
        //TODO
    }

    fun onAlarmDeleteClick(id: Int) {
        viewModelScope.launch {
            deleteAlarmUseCase(id)
        }
    }

    suspend fun getOrderIndexByParentId(id: Int?): String {
        if(id == null) return ""
        return (getOrderIndexByParentIdUseCase(id)+ 1).toString()
    }
}


data class MainScreenUiContentState(
    val temp: String
)
