package com.example.alarmer.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.alarmer.core.domain.data.alarm.DayOfWeek
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.alarmer.core.domain.data.alarm.AlarmTask
import com.example.alarmer.core.domain.data.alarm.AlarmTaskType
import com.example.alarmer.core.domain.ui.usecase.AlarmCreatorScreen.AlarmCreatorScreenViewModel

@Composable
fun AlarmCreatorScreen(
    viewModel: AlarmCreatorScreenViewModel = hiltViewModel()
) {
    AlarmCreatorScreenContent(viewModel)
}

@Composable
fun AlarmCreatorScreenContent(
    viewModel: AlarmCreatorScreenViewModel
) {
    val state = viewModel.state.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {

        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterStart){
            Text("Alarm Creator", fontWeight = FontWeight.Bold)
        }

        Spacer(Modifier.height(20.dp))
        // -------- TIME --------
        Text("Time", fontWeight = FontWeight.Bold)

        Row(verticalAlignment = Alignment.CenterVertically) {

            BasicTextField(
                value = state.value.hour,
                onValueChange = { viewModel.onHourChanged(it) },
                modifier = Modifier
                    .width(60.dp)
                    .padding(end = 8.dp)
            )

            Text(":")

            BasicTextField(
                value = state.value.minute,
                onValueChange = { viewModel.onMinuteChanged(it) },
                modifier = Modifier
                    .width(60.dp)
                    .padding(start = 8.dp)
            )
        }

        Spacer(Modifier.height(20.dp))

        // -------- LABEL --------
        Text("Label", fontWeight = FontWeight.Bold)

        BasicTextField(
            value = state.value.label,
            onValueChange = { viewModel.onLabelChanged(it)  },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )

        Spacer(Modifier.height(20.dp))

        // -------- DAYS OF WEEK --------
        Text("Repeat Days", fontWeight = FontWeight.Bold)

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            DayOfWeek.entries.forEach { day ->
                var result = state.value.repeatDays?.contains(day) == true
                DayButton(
                    day = day,
                    isSelected = result,
                    onClick = {
                        viewModel.onDayToggle(day)
                    }
                )
            }
        }

        Spacer(Modifier.height(20.dp))

        // -------- TASK --------
        Text("Task", fontWeight = FontWeight.Bold)

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            IconButton(onClick = { viewModel.onPhotoTask() }) {
                Icon(Icons.Default.AccountBox, contentDescription = "Photo task")
            }

            IconButton(onClick = { viewModel.onMathTask()}) {
                Icon(Icons.Default.AddCircle, contentDescription = "Math task")
            }
        }

        Spacer(Modifier.height(20.dp))

        // -------- DISABLE BEFORE --------
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = /* TODO: viewModel.state.disableEnabled */ false,
                onCheckedChange = { /* TODO: viewModel.onDisableEnabledChanged(it) */ }
            )
            Text("Disable before (ms)")
        }

        // Якщо увімкнено — показати поле
        if (/* TODO: viewModel.state.disableEnabled */ false) {
            BasicTextField(
                value = /* TODO: viewModel.state.disableMillis */ "",
                onValueChange = { /* TODO: viewModel.onDisableMillisChanged(it) */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )
        }

        Spacer(Modifier.height(20.dp))

        // -------- AUDIO --------
        Text("Audio", fontWeight = FontWeight.Bold)

        Text("Sound URI:")
        BasicTextField(
            value = /* TODO: viewModel.state.soundUri */ "",
            onValueChange = { /* TODO: viewModel.onSoundUriChanged(it) */ },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )

        Text("Volume (0.0 – 1.0)")
        BasicTextField(
            value = /* TODO: viewModel.state.volume */ "",
            onValueChange = { /* TODO: viewModel.onVolumeChanged(it) */ },
            modifier = Modifier
                .width(80.dp)
                .padding(vertical = 8.dp)
        )

        Spacer(Modifier.height(30.dp))

        // -------- SAVE BUTTON --------
        Button(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            onClick = {
                // TODO: viewModel.onSaveAlarm()
            }
        ) {
            Text("Create Alarm")
        }
    }
}

@Composable
private fun DayButton(
    day: DayOfWeek,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val letter = when(day) {
        DayOfWeek.MON -> "M"
        DayOfWeek.TUE -> "T"
        DayOfWeek.WED -> "W"
        DayOfWeek.THU -> "T"
        DayOfWeek.FRI -> "F"
        DayOfWeek.SAT -> "S"
        DayOfWeek.SUN -> "S"
    }

    val color = if (isSelected) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.surfaceVariant
    }

    Surface(
        modifier = Modifier.size(40.dp),
        color = color,
        shape = MaterialTheme.shapes.small,
        onClick = onClick
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(letter)
        }
    }
}
