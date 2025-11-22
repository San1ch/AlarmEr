package com.example.alarmer.core.ui.navigation

import com.example.alarmer.ui.screen.AlarmCreatorScreenRoute
import com.example.alarmer.ui.screen.MainScreenRoute

sealed class NavigationTarget(open val route: String) {
    object NullScreen : NavigationTarget(route = "")

    sealed class MainScreens(route: String) : NavigationTarget(route) {
        object Main : MainScreens(route = MainScreenRoute)
    }

    sealed class AlarmCreatorScreens(route: String) : NavigationTarget(route) {
        object Main : MainScreens(route = AlarmCreatorScreenRoute)
    }
}