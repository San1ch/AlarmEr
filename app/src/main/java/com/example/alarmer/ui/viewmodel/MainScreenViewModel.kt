package com.example.alarmer.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.alarmer.core.domain.data.navigation.NavigationTarget
import com.example.alarmer.core.domain.ui.usecase.MainScreen.CreateAlarmUseCase
import com.example.alarmer.core.domain.ui.usecase.navigation.NavigateToScreenUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private var navigateToScreenUseCase: NavigateToScreenUseCase
): ViewModel() {

    private val _state = MutableStateFlow(MainScreenState(""))
    val state: StateFlow<MainScreenState> = _state.asStateFlow()



    fun onCreateAlarmClick(){
        navigateToScreenUseCase(NavigationTarget.AlarmCreatorScreens.Main)
    }
}

data class MainScreenState(
    val temp: String
)
