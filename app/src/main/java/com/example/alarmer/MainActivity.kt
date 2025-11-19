package com.example.alarmer

import com.example.alarmer.ui.viewmodel.CenterProgramViewModel
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.alarmer.core.domain.data.navigation.NavigationTarget
import com.example.alarmer.ui.screen.AlarmCreatorScreen
import com.example.alarmer.ui.screen.AlarmCreatorScreenRoute
import com.example.alarmer.ui.screen.MainScreen
import com.example.alarmer.ui.screen.MainScreenRoute
import com.example.alarmer.ui.theme.AlarmErTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AlarmErTheme {
                CenterProgramScreen()
            }
        }
    }
}

@Composable
fun CenterProgramScreen(viewModel: CenterProgramViewModel = hiltViewModel()){
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination  = MainScreenRoute,
        modifier = Modifier.fillMaxSize()
    ){
        composable(MainScreenRoute){ MainScreen()}
        composable(AlarmCreatorScreenRoute){ AlarmCreatorScreen() }
    }

    CenterProgramNavigation(viewModel, navController)

}

@Composable
fun CenterProgramNavigation(viewModel: CenterProgramViewModel, navController: NavController) {
    val navigationTarget = viewModel.navigationTarget.collectAsState()
    val shouldNavigateBack = viewModel.shouldNavigateBack.collectAsState()
    if (navigationTarget.value !is NavigationTarget.NullScreen) {
        navController.navigate(navigationTarget.value.route)
        viewModel.resetNavigationTarget()
    }

    if (shouldNavigateBack.value) {
        navController.popBackStack()
        viewModel.resetBackMode()
    }
}