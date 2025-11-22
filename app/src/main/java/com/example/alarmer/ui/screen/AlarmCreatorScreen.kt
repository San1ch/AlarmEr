package com.example.alarmer.ui.screen

import android.content.Context
import android.provider.OpenableColumns
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.example.alarmer.core.domain.data.alarm.DayOfWeek
import com.example.alarmer.core.domain.data.alarm.TimeMode
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

    AlarmCreatorScreenContent(viewModel, audioPickerLauncher)

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
) {
    val state by viewModel.stateUiContent.collectAsState()
    val context = LocalContext.current

    val audioTitle = remember(state.soundUri) {
        deriveAudioTitle(context, state.soundUri)
    }

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
        // ===== НЕСКРОЛЬОВАНА ВЕРХНЯ ЧАСТИНА =====

        Spacer(Modifier.height(8.dp))

        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                "Alarm Creator",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.headlineSmall,
                color = Color.White
            )
        }

        Spacer(Modifier.height(20.dp))

        // LABEL
        Text("Label", fontWeight = FontWeight.Bold, color = Color.White)

        OutlinedTextField(
            value = state.label,
            onValueChange = { viewModel.onLabelChanged(it) },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            textStyle = MaterialTheme.typography.bodyMedium.copy(color = Color.White)
        )

        Spacer(Modifier.height(16.dp))

        // TIME
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text("Time mode", fontWeight = FontWeight.Bold, color = Color.White)
            Spacer(Modifier.width(8.dp))
            TimeModeToggle(
                selected = state.timeMode,
                onToggle = { viewModel.onTimeModeToggle() }
            )
        }

        if (state.timeMode == TimeMode.LINKED) {

        }

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
                label = { Text("HH", color = Color.White) },
                singleLine = true,
                modifier = Modifier.width(80.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                textStyle = MaterialTheme.typography.bodyMedium.copy(color = Color.White)
            )

            Text(":", color = Color.White)

            OutlinedTextField(
                value = state.minute,
                onValueChange = {
                    val filtered = it.filter { ch -> ch.isDigit() }.take(2)
                    viewModel.onMinuteChanged(filtered)
                },
                label = { Text("MM", color = Color.White) },
                singleLine = true,
                modifier = Modifier.width(80.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                textStyle = MaterialTheme.typography.bodyMedium.copy(color = Color.White)
            )
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
                    Text("Repeat Days", fontWeight = FontWeight.Bold, color = Color.White)
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
                    Text("Task", fontWeight = FontWeight.Bold, color = Color.White)

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        IconButton(onClick = { viewModel.onPhotoTask() }) {
                            Icon(
                                imageVector = Icons.Default.AccountBox,
                                contentDescription = "Photo task",
                                tint = Color.White
                            )
                        }

                        IconButton(onClick = { viewModel.onMathTask() }) {
                            Icon(
                                imageVector = Icons.Default.AddCircle,
                                contentDescription = "Math task",
                                tint = Color.White
                            )
                        }
                    }
                }

                // DISABLE BEFORE
                item {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        androidx.compose.material3.Checkbox(
                            checked = state.enabledDisableMode,
                            onCheckedChange = { viewModel.onEnabledDisableModeChanged(it) }
                        )
                        Text("Disable before", color = Color.White)
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
                                label = { Text("HH", color = Color.White) },
                                singleLine = true,
                                modifier = Modifier.width(80.dp),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                textStyle = MaterialTheme.typography.bodyMedium.copy(color = Color.White)
                            )

                            Text(":", color = Color.White)

                            OutlinedTextField(
                                value = state.disableMinute,
                                onValueChange = {
                                    val filtered = it.filter { ch -> ch.isDigit() }.take(2)
                                    viewModel.onDisableMinuteChanged(filtered)
                                },
                                label = { Text("MM", color = Color.White) },
                                singleLine = true,
                                modifier = Modifier.width(80.dp),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                textStyle = MaterialTheme.typography.bodyMedium.copy(color = Color.White)
                            )
                        }
                    }
                }

                // AUDIO
                item {
                    Text("Audio", fontWeight = FontWeight.Bold, color = Color.White)

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
                            color = Color.White
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

        // ===== НИЖНІ КНОПКИ =====

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

@Composable
private fun VolumeSlider(value: Float, onValueChange: (Float) -> Unit) {
    Column {
        Text("Volume", color = Color.White)
        Slider(
            value = value,
            onValueChange = { onValueChange(it) },
            valueRange = 0f..1f,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun TimeModeToggle(
    selected: TimeMode,
    onToggle: () -> Unit
) {
    BoxWithConstraints(
        modifier = Modifier
            .clip(RoundedCornerShape(999.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .clickable { onToggle() }
            .padding(4.dp)
            .height(32.dp)
    ) {
        val segmentWidth = maxWidth / 2
        val targetOffset = if (selected == TimeMode.STANDARD) 0.dp else segmentWidth

        val offsetX by animateDpAsState(
            targetValue = targetOffset,
            animationSpec = tween(
                durationMillis = 200,
                easing = FastOutSlowInEasing
            ),
            label = "timeModePillOffset"
        )

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
            Text(
                text = "Standard",
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                color = if (selected == TimeMode.STANDARD) Color.DarkGray else Color.LightGray,
                fontSize = 13.sp
            )
            Text(
                text = "Linked",
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                color = if (selected == TimeMode.LINKED) Color.DarkGray else Color.LightGray,
                fontSize = 13.sp
            )
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
            Text(letter, color = Color.White)
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
