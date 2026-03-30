package com.example.tools

import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateCalculatorScreen() {
    var startDate by remember { mutableStateOf(LocalDate.now()) }
    var shiftAmount by remember { mutableStateOf("") }
    var selectedUnit by remember { mutableStateOf("Dni") }
    var expandedUnitMenu by remember { mutableStateOf(false) }

    var resultDate by remember { mutableStateOf<LocalDate?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val context = LocalContext.current
    val dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy G", Locale.forLanguageTag("pl-PL"))

    val calendar = Calendar.getInstance()
    calendar.set(startDate.year, startDate.monthValue - 1, startDate.dayOfMonth)
    val datePickerDialog = DatePickerDialog(
        context,
        { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
            startDate = LocalDate.of(year, month + 1, dayOfMonth)
            resultDate = null
            errorMessage = null
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        OutlinedTextField(
            value = startDate.format(dateFormatter),
            onValueChange = { },
            label = { Text("Data początkowa") },
            enabled = false,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { datePickerDialog.show() },
            colors = OutlinedTextFieldDefaults.colors(
                disabledTextColor = colorResource(id = R.color.bardzoJasnySzary),
                disabledBorderColor = colorResource(id = R.color.bardzoJasnySzary),
                disabledLabelColor = colorResource(id = R.color.bardzoJasnySzary)
            )
        )

        OutlinedTextField(
            value = shiftAmount,
            onValueChange = {
                shiftAmount = it
                errorMessage = null
                resultDate = null
            },
            label = { Text("Wartość przesunięcia") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedTextColor = colorResource(id = R.color.bardzoJasnySzary),
                unfocusedBorderColor = colorResource(id = R.color.bardzoJasnySzary),
                unfocusedLabelColor = colorResource(id = R.color.bardzoJasnySzary),
                focusedTextColor = colorResource(id = R.color.jasnyNiebieski),
                focusedBorderColor = colorResource(id = R.color.jasnyNiebieski),
                focusedLabelColor = colorResource(id = R.color.jasnyNiebieski),
                cursorColor = colorResource(id = R.color.jasnyNiebieski),
                selectionColors = TextSelectionColors(
                    handleColor = colorResource(id = R.color.jasnyNiebieski),
                    backgroundColor = colorResource(id = R.color.jasnyNiebieski).copy(alpha = 0.3f)
                )
            )
        )

        ExposedDropdownMenuBox(
            expanded = expandedUnitMenu,
            onExpandedChange = { expandedUnitMenu = !expandedUnitMenu }
        ) {
            OutlinedTextField(
                value = selectedUnit,
                onValueChange = {},
                readOnly = true,
                label = { Text("Jednostka") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedUnitMenu) },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedTextColor = colorResource(id = R.color.bardzoJasnySzary),
                    unfocusedBorderColor = colorResource(id = R.color.bardzoJasnySzary),
                    unfocusedLabelColor = colorResource(id = R.color.bardzoJasnySzary),
                    focusedTextColor = colorResource(id = R.color.jasnyNiebieski),
                    focusedBorderColor = colorResource(id = R.color.jasnyNiebieski),
                    focusedLabelColor = colorResource(id = R.color.jasnyNiebieski),
                )
            )
            ExposedDropdownMenu(
                expanded = expandedUnitMenu,
                onDismissRequest = { expandedUnitMenu = false },
                modifier = Modifier.background(colorResource(id = R.color.szary))
            ) {
                listOf("Dni", "Miesiące", "Lata").forEach { unit ->
                    DropdownMenuItem(
                        text = { Text(unit, color = colorResource(id = R.color.bardzoJasnySzary)) },
                        onClick = {
                            selectedUnit = unit
                            expandedUnitMenu = false
                            resultDate = null
                        }
                    )
                }
            }
        }

        Button(
            onClick = {
                val amount = shiftAmount.toLongOrNull()

                if (amount == null) {
                    errorMessage = "Podaj poprawną wartość liczbową."
                    resultDate = null
                    return@Button
                }

                if (amount > 100000 || amount < -100000) {
                    errorMessage = "Wartość przesunięcia jest zbyt duża (limit: +/- 100 000)."
                    resultDate = null
                    return@Button
                }

                errorMessage = null

                try {
                    resultDate = when (selectedUnit) {
                        "Dni" -> startDate.plusDays(amount)
                        "Miesiące" -> startDate.plusMonths(amount)
                        "Lata" -> startDate.plusYears(amount)
                        else -> startDate
                    }
                } catch (e: Exception) {
                    errorMessage = "Błąd krytyczny: Data poza obsługiwanym zakresem."
                    resultDate = null
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.niebieskiGlowny))
        ) {
            Text("Oblicz przesunięcie", color = colorResource(id = R.color.bardzoJasnySzary))
        }

        errorMessage?.let {
            Text(text = it, color = colorResource(id = R.color.czerwonyGlowny), fontWeight = FontWeight.Bold)
        }

        resultDate?.let {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.szary))
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Nowa data:", color = colorResource(id = R.color.bardzoJasnySzary))
                    Text(
                        text = it.format(dateFormatter),
                        style = MaterialTheme.typography.headlineMedium,
                        color = colorResource(id = R.color.bardzoJasnySzary),
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}