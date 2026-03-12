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
import androidx.compose.ui.res.colorResource
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
                label = { Text("Start - Data") },
                enabled = false,
                modifier = Modifier
                    .weight(1f)
                    .clickable { startDialog.show() },
                colors = OutlinedTextFieldDefaults.colors(
                    disabledTextColor = colorResource(id = R.color.bardzoJasnySzary),
                    disabledBorderColor = colorResource(id = R.color.bardzoJasnySzary),
                    disabledLabelColor = colorResource(id = R.color.bardzoJasnySzary)
                )
            )
            OutlinedTextField(
                value = startTime.format(timeFormatter),
                onValueChange = { },
                label = { Text("Godzina") },
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
                            contentDescription = "Ustaw na teraz",
                            tint = colorResource(id = R.color.jasnyNiebieski)
                        )
                    }
                },
                colors = OutlinedTextFieldDefaults.colors(
                    disabledTextColor = colorResource(id = R.color.bardzoJasnySzary),
                    disabledBorderColor = colorResource(id = R.color.bardzoJasnySzary),
                    disabledLabelColor = colorResource(id = R.color.bardzoJasnySzary)
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
                label = { Text("Koniec - Data") },
                enabled = false,
                modifier = Modifier
                    .weight(1f)
                    .clickable { endDialog.show() },
                colors = OutlinedTextFieldDefaults.colors(
                    disabledTextColor = colorResource(id = R.color.bardzoJasnySzary),
                    disabledBorderColor = colorResource(id = R.color.bardzoJasnySzary),
                    disabledLabelColor = colorResource(id = R.color.bardzoJasnySzary)
                )
            )
            OutlinedTextField(
                value = endTime.format(timeFormatter),
                onValueChange = { },
                label = { Text("Godzina") },
                enabled = false,
                modifier = Modifier
                    .weight(0.8f)
                    .clickable { endTimeDialog.show() },
                trailingIcon = {
                    // Guzik "Teraz" dla drugiej daty
                    IconButton(onClick = {
                        endDate = LocalDate.now()
                        endTime = LocalTime.now()
                        resultDuration = null
                    }) {
                        Icon(
                            imageVector = Icons.Filled.Refresh,
                            contentDescription = "Ustaw na teraz",
                            tint = colorResource(id = R.color.jasnyNiebieski)
                        )
                    }
                },
                colors = OutlinedTextFieldDefaults.colors(
                    disabledTextColor = colorResource(id = R.color.bardzoJasnySzary),
                    disabledBorderColor = colorResource(id = R.color.bardzoJasnySzary),
                    disabledLabelColor = colorResource(id = R.color.bardzoJasnySzary)
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
            colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.niebieskiGlowny))
        ) {
            Text("Oblicz różnicę", color = colorResource(id = R.color.bardzoJasnySzary))
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
                colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.szary))
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text("Rozbicie różnicy czasu:", color = colorResource(id = R.color.bardzoJasnySzary), fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(16.dp))

                    Text("Dni: $days", style = MaterialTheme.typography.bodyLarge, color = colorResource(id = R.color.jasnyNiebieski))
                    Text("Godzin: $hours", style = MaterialTheme.typography.bodyLarge, color = colorResource(id = R.color.jasnyNiebieski))
                    Text("Minut: $minutes", style = MaterialTheme.typography.bodyLarge, color = colorResource(id = R.color.jasnyNiebieski))
                    Text("Sekund: $seconds", style = MaterialTheme.typography.bodyLarge, color = colorResource(id = R.color.jasnyNiebieski))
                }
            }
        }
    }
}