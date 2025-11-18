package com.example.alarmer.core.domain.ui.usecase.navigation

import com.example.alarmer.core.domain.data.navigation.NavigationTarget
import com.example.alarmer.core.domain.service.CentralScreenService.NavigationCenter
import javax.inject.Inject

class NavigateToScreenUseCase @Inject constructor(
    private val navigationCenter: NavigationCenter
) {

    operator fun invoke(screen: NavigationTarget) {
        navigationCenter.navigate(screen)
    }
}