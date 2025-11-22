package com.example.alarmer.core.ui.usecase.navigation

import com.example.alarmer.core.ui.ViewModel.CentralScreenService.NavigationCenter
import javax.inject.Inject

class BackToPreviousScreenUseCase @Inject constructor(
    private val navigationCenter: NavigationCenter
){
    operator fun invoke() {
        navigationCenter.backToPreviousScreen()
    }
}
