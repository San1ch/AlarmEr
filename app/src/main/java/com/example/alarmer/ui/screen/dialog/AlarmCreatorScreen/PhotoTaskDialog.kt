package com.example.alarmer.ui.screen.dialog.AlarmCreatorScreen

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.alarmer.core.ui.ViewModel.PhotoTaskState

@Composable
fun PhotoTaskDialog(
    state: PhotoTaskState,
    onAddPhotoClick: () -> Unit,
    onPhotoClick: (String) -> Unit,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text("Choose a photo")
        },
        text = {
            Column {
                Text(
                    text = "Select one of your photos or add a new one.",
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.height(12.dp))

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    item {
                        Box(
                            modifier = Modifier
                                .size(80.dp)
                                .border(
                                    width = 2.dp,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                .clickable { onAddPhotoClick() },   // БУЛО: { onAddPhotoClick }
                            contentAlignment = Alignment.Center
                        ) {
                            Text("+")
                        }
                    }

                    items(state.photos) { uri ->
                        val isSelected = uri == state.selectedPhoto
                        val borderColor =
                            if (isSelected) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.outlineVariant

                        Box(
                            modifier = Modifier
                                .size(80.dp)
                                .border(2.dp, borderColor)
                                .clickable { onPhotoClick(uri) },
                            contentAlignment = Alignment.Center
                        ) {
                            Text("IMG")
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                enabled = state.selectedPhoto != null
            ) {
                Text("Use photo")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
