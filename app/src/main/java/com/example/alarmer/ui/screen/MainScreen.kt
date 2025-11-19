package com.example.alarmer.ui.screen

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.alarmer.ui.viewmodel.MainScreenViewModel
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
fun MainScreen(viewModel: MainScreenViewModel = hiltViewModel()) {
    MainScreenContent(viewModel)
}

@Composable
fun MainScreenContent(viewModel: MainScreenViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                WindowInsets.systemBars
                    .only(WindowInsetsSides.Top + WindowInsetsSides.Bottom)
                    .asPaddingValues()
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(19.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(text = "")
            }

            Spacer(modifier = Modifier.height(12.dp))


            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {

            }


            Button(
                onClick = { viewModel.onCreateAlarmClick() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .padding(top = 16.dp)
            ) {
                Text("Create Alarm")
            }
        }
    }
}
