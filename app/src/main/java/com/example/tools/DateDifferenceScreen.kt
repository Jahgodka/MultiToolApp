package com.example.tools

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateDifferenceScreen() {
    var startDate by remember { mutableStateOf(LocalDate.now()) }
    var startTime by remember { mutableStateOf(LocalTime.now()) }

    var endDate by remember { mutableStateOf(LocalDate.now().plusDays(1)) }
    var endTime by remember { mutableStateOf(LocalTime.of(12, 0)) }

    var resultDuration by remember { mutableStateOf<Duration?>(null) }

    val context = LocalContext.current
    val dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss")

    val startDialog = DatePickerDialog(
        context,
        { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
            startDate = LocalDate.of(year, month + 1, dayOfMonth)
            resultDuration = null
        },
        startDate.year, startDate.monthValue - 1, startDate.dayOfMonth
    )

    val endDialog = DatePickerDialog(
        context,
        { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
            endDate = LocalDate.of(year, month + 1, dayOfMonth)
            resultDuration = null
        },
        endDate.year, endDate.monthValue - 1, endDate.dayOfMonth
    )

    val startTimeDialog = TimePickerDialog(
        context,
        { _: TimePicker, hourOfDay: Int, minute: Int ->
            startTime = LocalTime.of(hourOfDay, minute, 0)
            resultDuration = null
        },
        startTime.hour, startTime.minute, true
    )

    val endTimeDialog = TimePickerDialog(
        context,
        { _: TimePicker, hourOfDay: Int, minute: Int ->
            endTime = LocalTime.of(hourOfDay, minute, 0)
            resultDuration = null
        },
        endTime.hour, endTime.minute, true
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = startDate.format(dateFormatter),
                onValueChange = { },
                label = { Text(stringResource(id = R.string.label_start_date)) },
                enabled = false,
                modifier = Modifier
                    .weight(1f)
                    .clickable { startDialog.show() },
                colors = OutlinedTextFieldDefaults.colors(
                    disabledTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    disabledBorderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
            OutlinedTextField(
                value = startTime.format(timeFormatter),
                onValueChange = { },
                label = { Text(stringResource(id = R.string.label_time)) },
                enabled = false,
                modifier = Modifier
                    .weight(0.8f)
                    .clickable { startTimeDialog.show() },
                trailingIcon = {
                    IconButton(onClick = {
                        startDate = LocalDate.now()
                        startTime = LocalTime.now()
                        resultDuration = null
                    }) {
                        Icon(
                            imageVector = Icons.Filled.Refresh,
                            contentDescription = stringResource(id = R.string.desc_set_now),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                colors = OutlinedTextFieldDefaults.colors(
                    disabledTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    disabledBorderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = endDate.format(dateFormatter),
                onValueChange = { },
                label = { Text(stringResource(id = R.string.label_time)) },
                enabled = false,
                modifier = Modifier
                    .weight(1f)
                    .clickable { endDialog.show() },
                colors = OutlinedTextFieldDefaults.colors(
                    disabledTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    disabledBorderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
            OutlinedTextField(
                value = endTime.format(timeFormatter),
                onValueChange = { },
                label = { Text(stringResource(id = R.string.label_time)) },
                enabled = false,
                modifier = Modifier
                    .weight(0.8f)
                    .clickable { endTimeDialog.show() },
                trailingIcon = {
                    IconButton(onClick = {
                        endDate = LocalDate.now()
                        endTime = LocalTime.now()
                        resultDuration = null
                    }) {
                        Icon(
                            imageVector = Icons.Filled.Refresh,
                            contentDescription = stringResource(id = R.string.desc_set_now),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                colors = OutlinedTextFieldDefaults.colors(
                    disabledTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    disabledBorderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
        }

        Button(
            onClick = {
                val startDateTime = LocalDateTime.of(startDate, startTime)
                val endDateTime = LocalDateTime.of(endDate, endTime)

                resultDuration = Duration.between(startDateTime, endDateTime).abs()
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
            Text(stringResource(id = R.string.btn_calculate_diff), color = MaterialTheme.colorScheme.onPrimary)
        }

        resultDuration?.let { duration ->
            val days = duration.toDays()
            val hours = duration.toHours() % 24
            val minutes = duration.toMinutes() % 60
            val seconds = (duration.toMillis() / 1000) % 60

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(stringResource(id = R.string.title_time_breakdown), color = MaterialTheme.colorScheme.onSurfaceVariant, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(16.dp))

                    Text(stringResource(id = R.string.label_days, days), style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.primary)
                    Text(stringResource(id = R.string.label_hours, hours), style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.primary)
                    Text(stringResource(id = R.string.label_minutes, minutes), style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.primary)
                    Text(stringResource(id = R.string.label_seconds, seconds), style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.primary)
                }
            }
        }
    }
}