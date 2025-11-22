package com.example.alarmer.core.ui.usecase.navigation

import com.example.alarmer.core.ui.navigation.NavigationTarget
import com.example.alarmer.core.ui.ViewModel.CentralScreenService.NavigationCenter
import javax.inject.Inject

class NavigateToScreenUseCase @Inject constructor(
    private val navigationCenter: NavigationCenter
) {

    operator fun invoke(screen: NavigationTarget) {
        navigationCenter.navigate(screen)
    }
}