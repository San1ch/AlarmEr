package com.example.alarmer.core.ui.ViewModel.CentralScreenService

import com.example.alarmer.core.ui.navigation.NavigationTarget
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NavigationCenter @Inject constructor() {
    //navigation
    private val _shouldNavigateBack = MutableStateFlow(false)
    val shouldNavigateBack: StateFlow<Boolean> = _shouldNavigateBack

    private val _navTarget = MutableStateFlow<NavigationTarget>(NavigationTarget.NullScreen)
    val navTarget: StateFlow<NavigationTarget> = _navTarget

    fun navigate(target: NavigationTarget) {
        _navTarget.value = target
    }

    fun backToPreviousScreen() {
        _shouldNavigateBack.value = true
    }

    fun resetBackMode() {
        _shouldNavigateBack.value = false
    }

    fun resetTarget() {
        _navTarget.value = NavigationTarget.NullScreen
    }
}