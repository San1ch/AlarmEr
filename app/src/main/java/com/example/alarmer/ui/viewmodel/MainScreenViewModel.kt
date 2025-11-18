package com.example.alarmer.ui.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor(): ViewModel() {
    private val _state = MutableStateFlow(MainScreenState(""))
    val state: StateFlow<MainScreenState> = _state.asStateFlow()
}

data class MainScreenState(
    val temp: String
)
