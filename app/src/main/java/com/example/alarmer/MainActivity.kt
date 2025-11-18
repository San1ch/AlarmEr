package com.example.alarmer

import CenterProgramViewModel
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.alarmer.ui.screen.MainScreen.MainScreen
import com.example.alarmer.ui.screen.MainScreenRoute
import com.example.alarmer.ui.theme.AlarmErTheme

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
    }}