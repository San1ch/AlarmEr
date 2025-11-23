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
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel

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
            .background(MaterialTheme.colorScheme.background) // темний фон
            .padding(
                WindowInsets.systemBars
                    .only(WindowInsetsSides.Top + WindowInsetsSides.Bottom)
                    .asPaddingValues()
            ), horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Alarms",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onSurface, modifier = Modifier.padding(vertical = 12.dp)
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(text = "")
            }
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                items(alarms.count()) { index ->
                    val alarm = alarms[index]

                    val orderIndex by produceState(initialValue = "") {
                        value = viewModel.getOrderIndexByParentId(alarm.linkedToId)
                    }

                    AlarmItem(
                        alarm = alarm,
                        onEnabledChange = { viewModel.onAlarmEnabledChange(alarm.id, it) },
                        onEditClick = { viewModel.onAlarmEditClick(alarm.id) },
                        onDeleteClick = { viewModel.onAlarmDeleteClick(alarm.id) }, // зроби такий метод у VM
                        modifier = Modifier.padding(vertical = 6.dp),
                        orderIndex = orderIndex
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
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier,
    orderIndex: String,
) {
    val cardColor =
        if (alarm.isEnabled) MaterialTheme.colorScheme.surface
        else MaterialTheme.colorScheme.surfaceVariant

    val textColor = MaterialTheme.colorScheme.onSurface

    // local menu state
    var menuExpanded by remember { mutableStateOf(false) }

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
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "#" + (alarm.orderIndex + 1).toString(),
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = textColor
                    ),
                    modifier = Modifier.padding(end = 8.dp)
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
                        text = formatTime(alarm.getHour(), alarm.getMinute()),
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = textColor
                        )
                    )
                }

                // правий стовпчик: switch + три крапки
                Column(
                    horizontalAlignment = Alignment.End
                ) {
                    Switch(
                        checked = alarm.isEnabled,
                        onCheckedChange = onEnabledChange
                    )

                    Box { // anchor for dropdown
                        IconButton(onClick = { menuExpanded = true }) {
                            Icon(
                                imageVector = Icons.Default.MoreVert,
                                contentDescription = "More actions",
                                tint = textColor
                            )
                        }

                        DropdownMenu(
                            expanded = menuExpanded,
                            onDismissRequest = { menuExpanded = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("Edit") },
                                onClick = {
                                    menuExpanded = false
                                    onEditClick()
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Delete") },
                                onClick = {
                                    menuExpanded = false
                                    onDeleteClick()
                                }
                            )
                        }
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
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = textColor.copy(alpha = 0.85f)
                    )
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            val taskText = when (alarm.task?.type) {
                null -> null
                AlarmTaskType.PHOTO -> "Task: Photo"
                AlarmTaskType.MATH -> "Task: Math"
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (taskText != null) {
                    Text(
                        text = taskText,
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.primary
                        )
                    )
                }
                if (alarm.linkedToId != null) {
                    Text("Linked to: #$orderIndex")
                }
            }
        }
    }
}


/*  TODO creating logic
        2. Need fix photo preview

    TODO main logic
        2. Need make drag and drop system for alarms
        3. Add logic for switch
        4. Make logic for dots settings button



 */

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
        if (repeatDays != emptyList<DayOfWeek>()) {
            DayOfWeek.entries.forEach { day ->
                val isActive = repeatDays.contains(day)
                DayChip(day = day, isActive = isActive)
            }
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