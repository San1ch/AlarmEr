package com.example.alarmer.core.domain.data.navigation

import com.example.alarmer.ui.screen.MainScreenRoute

sealed class NavigationTarget(open val route: String) {
    object NullScreen : NavigationTarget(route = "")

    sealed class MainScreens(route: String) : NavigationTarget(route) {
        object Main : MainScreens(route = MainScreenRoute)
    }
}