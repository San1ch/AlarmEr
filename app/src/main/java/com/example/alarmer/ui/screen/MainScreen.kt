package com.example.alarmer.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.material3.Switch
import androidx.compose.material3.IconButton
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.alarmer.core.domain.data.alarm.AlarmEntity
import com.example.alarmer.core.domain.data.alarm.AlarmTaskType
import com.example.alarmer.core.domain.data.alarm.DayOfWeek
import com.example.alarmer.core.ui.ViewModel.MainScreenViewModel
import androidx.compose.runtime.getValue

@Composable
fun MainScreen(viewModel: MainScreenViewModel = hiltViewModel()) {
    MainScreenContent(viewModel)
}

@Composable
fun MainScreenContent(viewModel: MainScreenViewModel) {
    val alarms by viewModel.alarms.collectAsState(emptyList())
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
                items(alarms.count()) { index ->
                    AlarmItem(
                        alarm = alarms[index],
                        onEnabledChange = { viewModel.onAlarmEnabledChange(alarms[index].id, it) },
                        onMenuClick = { viewModel.onAlarmEditClick(alarms[index].id) },
                        modifier = Modifier.padding(vertical = 6.dp)
                    )
                }
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


@Composable
private fun AlarmItem(
    alarm: AlarmEntity,
    onEnabledChange: (Boolean) -> Unit,
    onMenuClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val cardColor =
        if (alarm.isEnabled) MaterialTheme.colorScheme.surface
        else MaterialTheme.colorScheme.surfaceVariant

    val textColor = MaterialTheme.colorScheme.onSurface

    Surface(
        modifier = modifier.fillMaxWidth(),
        color = cardColor,
        shape = RoundedCornerShape(16.dp),
        tonalElevation = 4.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 10.dp)
        ) {
            // Верхній рядок: іконка + label + час + switch + меню
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = "Alarm icon",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .size(28.dp)
                        .padding(end = 8.dp)
                )

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = if (alarm.label.isNotBlank()) alarm.label else "No label",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = textColor
                        )
                    )

                    Text(
                        text = formatTime(alarm.hour, alarm.minute),
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = textColor
                        )
                    )
                }

                Column(
                    horizontalAlignment = Alignment.End
                ) {
                    Switch(
                        checked = alarm.isEnabled,
                        onCheckedChange = onEnabledChange
                    )

                    IconButton(onClick = onMenuClick) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "More actions",
                            tint = textColor
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            DaysRow(
                repeatDays = alarm.repeatDays,
                modifier = Modifier.fillMaxWidth()
            )

            val disableText = formatDisableText(alarm.disableHour, alarm.disableMinute)
            if (disableText != null) {
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = disableText,
                    style = MaterialTheme.typography.bodySmall.copy(color = textColor.copy(alpha = 0.85f))
                )
            }

            val taskText = when (alarm.task?.type) {
                null -> null
                AlarmTaskType.PHOTO -> "Task: Photo"
                AlarmTaskType.MATH -> "Task: Math"
            }

            if (taskText != null) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = taskText,
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.primary
                    )
                )
            }
        }
    }
}

@Composable
private fun DaysRow(
    repeatDays: List<DayOfWeek>,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        DayOfWeek.entries.forEach { day ->
            val isActive = repeatDays.contains(day)
            DayChip(day = day, isActive = isActive)
        }
    }
}

@Composable
private fun DayChip(
    day: DayOfWeek,
    isActive: Boolean
) {
    val letter = when (day) {
        DayOfWeek.MON -> "M"
        DayOfWeek.TUE -> "T"
        DayOfWeek.WED -> "W"
        DayOfWeek.THU -> "T"
        DayOfWeek.FRI -> "F"
        DayOfWeek.SAT -> "S"
        DayOfWeek.SUN -> "S"
    }

    val bgColor: Color
    val contentColor: Color

    if (isActive) {
        bgColor = MaterialTheme.colorScheme.primary
        contentColor = MaterialTheme.colorScheme.onPrimary
    } else {
        bgColor = MaterialTheme.colorScheme.surfaceVariant
        contentColor = MaterialTheme.colorScheme.onSurfaceVariant
    }

    Box(
        modifier = Modifier
            .size(28.dp)
            .background(bgColor, shape = CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = letter,
            style = MaterialTheme.typography.labelMedium,
            color = contentColor
        )
    }
}


private fun formatTime(hour: Int, minute: Int): String {
    return "%02d:%02d".format(hour, minute)
}

private fun formatDisableText(disableHour: Int, disableMinute: Int): String? {
    val totalMinutes = disableHour * 60 + disableMinute
    if (totalMinutes <= 0) return null

    val h = totalMinutes / 60
    val m = totalMinutes % 60

    val builder = StringBuilder("You can disable only ")

    when {
        h > 0 && m > 0 -> builder.append("$h h $m min")
        h > 0 -> builder.append("$h h")
        else -> builder.append("$m min")
    }

    builder.append(" before it rings")

    return builder.toString()
}