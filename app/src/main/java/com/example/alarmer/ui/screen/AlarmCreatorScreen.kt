package com.example.alarmer.ui.screen

import android.content.Context
import android.provider.OpenableColumns
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.alarmer.core.domain.data.alarm.AlarmEntity
import com.example.alarmer.core.domain.data.alarm.DayOfWeek
import com.example.alarmer.core.domain.data.alarm.TimeMode
import com.example.alarmer.core.ui.ViewModel.AlarmCreatorScreenUiContentState
import com.example.alarmer.core.ui.ViewModel.AlarmCreatorScreenViewModel
import com.example.alarmer.ui.screen.dialog.AlarmCreatorScreen.PhotoTaskDialog

@Composable
fun AlarmCreatorScreen(
    viewModel: AlarmCreatorScreenViewModel = hiltViewModel()
) {
    val state = viewModel.stateUiContent.collectAsState()

    val audioPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) {
            viewModel.onSoundUriChanged(uri.toString())
        }
    }

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) {
            viewModel.onPhotoAdded(uri.toString())
        }
    }

    AlarmCreatorScreenContent(viewModel, audioPickerLauncher, state.value)

    if (state.value.photoTaskState.isDialogVisible) {
        PhotoTaskDialog(
            state = state.value.photoTaskState,
            onAddPhotoClick = {
                photoPickerLauncher.launch("image/*")
            },
            onPhotoClick = { uri ->
                viewModel.onPhotoThumbnailClick(uri)
            },
            onConfirm = { viewModel.onPhotoDialogConfirm() },
            onDismiss = { viewModel.onPhotoDialogCancel() }
        )
    }
}

@Composable
fun AlarmCreatorScreenContent(
    viewModel: AlarmCreatorScreenViewModel,
    audioPickerLauncher: ActivityResultLauncher<String>,
    state: AlarmCreatorScreenUiContentState,
) {
    val context = LocalContext.current

    val audioTitle = remember(state.soundUri) {
        deriveAudioTitle(context, state.soundUri)
    }

    CompositionLocalProvider(
        LocalContentColor provides MaterialTheme.colorScheme.onBackground
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(horizontal = 20.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {

            Spacer(Modifier.height(8.dp))

            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "Alarm Creator",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.headlineSmall,
                )
            }

            Spacer(Modifier.height(20.dp))

            // LABEL
            Text("Label", fontWeight = FontWeight.Bold)

            OutlinedTextField(
                value = state.label,
                onValueChange = { viewModel.onLabelChanged(it) },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                textStyle = MaterialTheme.typography.bodyMedium
            )

            Spacer(Modifier.height(16.dp))

            // TIME MODE
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text("Time mode", fontWeight = FontWeight.Bold)
                Spacer(Modifier.width(8.dp))
                TimeModeToggle(
                    selected = state.timeMode,
                    onToggle = { viewModel.onTimeModeToggle() }
                )
            }

            val timeModeHeight =
                animateDpAsState(targetValue = if (state.timeMode == TimeMode.LINKED) 120.dp else 0.dp)

            AnimatedVisibility(
                visible = state.timeMode == TimeMode.LINKED,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                val alarms by viewModel.alarms.collectAsState(emptyList())
                if (alarms.isEmpty()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(40.dp)
                            .padding(start = 16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text("No alarms")
                    }
                } else {
                    LazyRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp),
                    ) {
                        items(alarms.size) { index ->
                            Box(modifier = Modifier.padding(start = 8.dp, end = 8.dp, top = 8.dp)) {
                                AlarmItem(
                                    onClick = { viewModel.onAlarmClick(alarms[index]) },
                                    isClicked = state.alarm == alarms[index],
                                    label = alarms[index].label,
                                    time = "%02d:%02d".format(
                                        alarms[index].getHour(),
                                        alarms[index].getMinute()
                                    )
                                )
                            }
                        }
                    }
                }
            }


            // TIME INPUT
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = state.hour,
                    onValueChange = {
                        val filtered = it.filter { ch -> ch.isDigit() }.take(2)
                        viewModel.onHourChanged(filtered)
                    },
                    label = { Text("HH") },
                    singleLine = true,
                    modifier = Modifier.width(80.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    textStyle = MaterialTheme.typography.bodyMedium
                )

                Text(":")

                OutlinedTextField(
                    value = state.minute,
                    onValueChange = {
                        val filtered = it.filter { ch -> ch.isDigit() }.take(2)
                        viewModel.onMinuteChanged(filtered)
                    },
                    label = { Text("MM") },
                    singleLine = true,
                    modifier = Modifier.width(80.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    textStyle = MaterialTheme.typography.bodyMedium
                )

                if (state.timeMode == TimeMode.LINKED) {
                    Spacer(Modifier.width(8.dp))
                    LinkedOffsetToggle(
                        isAfter = state.isAlarmRingAfter,
                        onToggle = { viewModel.onLinkedOffsetModeToggle() }
                    )
                }
            }


            Spacer(Modifier.height(20.dp))

            Box(
                modifier = Modifier
                    .weight(1f)
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {

                    // REPEAT DAYS
                    item {
                        Text("Repeat Days", fontWeight = FontWeight.Bold)
                        Spacer(Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            DayOfWeek.entries.forEach { day ->
                                val isSelected = state.repeatDays.contains(day)
                                DayButton(
                                    day = day,
                                    isSelected = isSelected,
                                    onClick = { viewModel.onDayToggle(day) }
                                )
                            }
                        }
                    }

                    // TASK
                    item {
                        Text("Task", fontWeight = FontWeight.Bold)

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            IconButton(onClick = { viewModel.onPhotoTask() }) {
                                Icon(
                                    imageVector = Icons.Default.AccountBox,
                                    contentDescription = "Photo task",
                                )
                            }

                            IconButton(onClick = { viewModel.onMathTask() }) {
                                Icon(
                                    imageVector = Icons.Default.AddCircle,
                                    contentDescription = "Math task",
                                )
                            }
                        }
                    }

                    // DISABLE BEFORE
                    item {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Checkbox(
                                checked = state.enabledDisableMode,
                                onCheckedChange = { viewModel.onEnabledDisableModeChanged(it) }
                            )
                            Text("Disable before")
                        }

                        if (state.enabledDisableMode) {
                            Spacer(Modifier.height(8.dp))

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                OutlinedTextField(
                                    value = state.disableHour,
                                    onValueChange = {
                                        val filtered = it.filter { ch -> ch.isDigit() }.take(2)
                                        viewModel.onDisableHourChanged(filtered)
                                    },
                                    label = { Text("HH") },
                                    singleLine = true,
                                    modifier = Modifier.width(80.dp),
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    textStyle = MaterialTheme.typography.bodyMedium
                                )

                                Text(":")

                                OutlinedTextField(
                                    value = state.disableMinute,
                                    onValueChange = {
                                        val filtered = it.filter { ch -> ch.isDigit() }.take(2)
                                        viewModel.onDisableMinuteChanged(filtered)
                                    },
                                    label = { Text("MM") },
                                    singleLine = true,
                                    modifier = Modifier.width(80.dp),
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    textStyle = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }

                    // AUDIO
                    item {
                        Text("Audio", fontWeight = FontWeight.Bold)

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Button(
                                onClick = { audioPickerLauncher.launch("audio/*") }
                            ) {
                                Text("Choose Audio")
                            }

                            Text(
                                text = audioTitle,
                                modifier = Modifier.padding(start = 12.dp),
                            )
                        }
                    }

                    // VOLUME
                    item {
                        VolumeSlider(
                            value = state.volume,
                            onValueChange = { viewModel.onVolumeChanged(it) }
                        )
                    }
                }
            }

            Spacer(Modifier.height(12.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    modifier = Modifier.weight(1f),
                    onClick = {
                        viewModel.onCancelAlarmCreatorClick()
                    }
                ) {
                    Text("Cancel")
                }

                Button(
                    modifier = Modifier.weight(1f),
                    onClick = { viewModel.onSaveAlarm() }
                ) {
                    Text("Create Alarm")
                }
            }
        }
    }
}

@Composable
fun AlarmItem(onClick: () -> Unit, isClicked: Boolean, label: String, time: String) {
    val cardBorderWidth by animateDpAsState(
        targetValue = if (isClicked) 3.dp else 0.dp,
        label = "alarmItemRadius"
    )

    Box(
        modifier = Modifier
            .clickable(onClick = onClick)
            .height(100.dp)
            .width(160.dp)
            .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(8.dp))
            .border(
                width = cardBorderWidth,
                color = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(8.dp)
            ).padding(8.dp)
    ) {
        Text(label, Modifier.align(Alignment.TopStart))
        Text(time, Modifier.align(Alignment.Center))

    }
}
@Composable
private fun TimeModeToggle(
    selected: TimeMode,
    onToggle: () -> Unit
) {
    val selectedIndex = if (selected == TimeMode.STANDARD) 0 else 1

    PillToggle(
        options = listOf("Standard", "Linked"),
        selectedIndex = selectedIndex,
        onOptionSelected = { _ ->
            // we do not care which exactly was tapped, just flip mode
            onToggle()
        }
    )
}

@Composable
private fun LinkedOffsetToggle(
    isAfter: Boolean,
    onToggle: () -> Unit
) {
    val selectedIndex = if (isAfter) 1 else 0

    PillToggle(
        options = listOf("Before", "After"),
        selectedIndex = selectedIndex,
        onOptionSelected = { _ ->
            // toggle between before / after
            onToggle()
        },
        modifier = Modifier.width(140.dp)
    )
}


@Composable
private fun VolumeSlider(value: Float, onValueChange: (Float) -> Unit) {
    Column {
        Text("Volume")
        Slider(
            value = value,
            onValueChange = { onValueChange(it) },
            valueRange = 0f..1f,
            modifier = Modifier.fillMaxWidth()
        )
    }
}


@Composable
private fun PillToggle(
    options: List<String>,
    selectedIndex: Int,
    onOptionSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    BoxWithConstraints(
        modifier = modifier
            .clip(RoundedCornerShape(999.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .height(32.dp)
            .padding(4.dp)
    ) {
        val segmentWidth = maxWidth / options.size
        val targetOffset = segmentWidth * selectedIndex

        val offsetX by animateDpAsState(
            targetValue = targetOffset,
            animationSpec = tween(
                durationMillis = 200,
                easing = FastOutSlowInEasing
            ),
            label = "pillToggleOffset"
        )

        // moving pill
        Box(
            modifier = Modifier
                .offset(x = offsetX)
                .width(segmentWidth)
                .fillMaxHeight()
                .clip(RoundedCornerShape(999.dp))
                .background(MaterialTheme.colorScheme.primary)
        )

        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            options.forEachIndexed { index, label ->
                Text(
                    text = label,
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onOptionSelected(index) },
                    textAlign = TextAlign.Center,
                    color = if (index == selectedIndex) Color.DarkGray else Color.LightGray,
                    fontSize = 13.sp
                )
            }
        }
    }
}


@Composable
private fun DayButton(
    day: DayOfWeek,
    isSelected: Boolean,
    onClick: () -> Unit
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

    val containerColor = if (isSelected) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.surfaceVariant
    }

    Surface(
        modifier = Modifier.size(40.dp),
        color = containerColor,
        shape = MaterialTheme.shapes.small,
        onClick = onClick
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(letter)
        }
    }
}

private fun deriveAudioTitle(context: Context, uriString: String): String {
    if (uriString.isBlank()) return "No audio selected"

    return try {
        val uri = uriString.toUri()
        context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
            val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            if (nameIndex >= 0 && cursor.moveToFirst()) {
                cursor.getString(nameIndex)
            } else {
                uri.lastPathSegment ?: "Unknown audio"
            }
        } ?: "Unknown audio"
    } catch (e: Exception) {
        "Unknown audio"
    }
}
