package com.securityguard.app.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.securityguard.app.ui.theme.AppSpacing
import com.securityguard.app.ui.theme.AppTypography
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateTimePickerSheet(
    visible: Boolean,
    title: String,
    initial: Instant = Instant.now(),
    onDismiss: () -> Unit,
    onConfirm: (Instant) -> Unit,
) {
    if (!visible) return

    val zone = ZoneId.systemDefault()
    var localDate by remember(initial) {
        mutableStateOf(LocalDateTime.ofInstant(initial, zone).toLocalDate())
    }
    var localTime by remember(initial) {
        mutableStateOf(LocalDateTime.ofInstant(initial, zone).toLocalTime())
    }
    var showDatePicker by remember { mutableStateOf(true) }
    var showTimePicker by remember { mutableStateOf(false) }

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(onDismissRequest = onDismiss, sheetState = sheetState) {
        Column(Modifier.padding(AppSpacing.Standard)) {
            Text(title, style = AppTypography.Headline)
            Spacer(Modifier.height(AppSpacing.Standard))

            Text(
                localDate.atTime(localTime).format(DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm")),
                style = AppTypography.MonoBody,
            )
            Spacer(Modifier.height(AppSpacing.Close))

            if (showDatePicker) {
                val dateState = rememberDatePickerState(
                    initialSelectedDateMillis = localDate.atStartOfDay(zone).toInstant().toEpochMilli(),
                )
                DatePicker(state = dateState, modifier = Modifier.fillMaxWidth())
                Button(
                    onClick = {
                        dateState.selectedDateMillis?.let { millis ->
                            localDate = Instant.ofEpochMilli(millis).atZone(zone).toLocalDate()
                        }
                        showDatePicker = false
                        showTimePicker = true
                    },
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text("Next: Time")
                }
            }

            if (showTimePicker) {
                val timeState = rememberTimePickerState(
                    initialHour = localTime.hour,
                    initialMinute = localTime.minute,
                    is24Hour = true,
                )
                TimePicker(state = timeState, modifier = Modifier.fillMaxWidth())
                Button(
                    onClick = {
                        localTime = LocalTime.of(timeState.hour, timeState.minute)
                        val instant = localDate.atTime(localTime).atZone(zone).toInstant()
                        onConfirm(instant)
                        onDismiss()
                    },
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text("Confirm")
                }
            }

            TextButton(onClick = onDismiss, modifier = Modifier.fillMaxWidth()) {
                Text("Cancel")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuickDatePickerDialog(
    visible: Boolean,
    onDismiss: () -> Unit,
    onConfirm: (LocalDate) -> Unit,
) {
    if (!visible) return
    val state = rememberDatePickerState()
    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                state.selectedDateMillis?.let { millis ->
                    onConfirm(Instant.ofEpochMilli(millis).atZone(ZoneId.systemDefault()).toLocalDate())
                }
                onDismiss()
            }) { Text("OK") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        },
    ) {
        DatePicker(state = state)
    }
}
