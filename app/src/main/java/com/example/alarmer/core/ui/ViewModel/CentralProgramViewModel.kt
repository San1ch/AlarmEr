package com.example.alarmer.core.ui.ViewModel

import androidx.lifecycle.ViewModel
import com.example.alarmer.core.ui.ViewModel.CentralScreenService.NavigationCenter
import dagger.hilt.android.lifecycle.HiltViewModel

import javax.inject.Inject

@HiltViewModel
class CenterProgramViewModel @Inject constructor(
    private val navigationCenter: NavigationCenter) : ViewModel() {
    val navigationTarget = navigationCenter.navTarget
    val shouldNavigateBack = navigationCenter.shouldNavigateBack


    fun resetNavigationTarget() {
        navigationCenter.resetTarget()
    }

    fun resetBackMode() {
        navigationCenter.resetBackMode()
    }
}