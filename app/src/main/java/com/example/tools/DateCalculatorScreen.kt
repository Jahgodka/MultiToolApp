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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale

enum class DateUnit { DAYS, MONTHS, YEARS }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateCalculatorScreen() {
    var startDate by remember { mutableStateOf(LocalDate.now()) }
    var shiftAmount by remember { mutableStateOf("") }
    var selectedUnit by remember { mutableStateOf(DateUnit.DAYS) }
    var expandedUnitMenu by remember { mutableStateOf(false) }

    var resultDate by remember { mutableStateOf<LocalDate?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val context = LocalContext.current

    val dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy G", Locale.getDefault())

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

    val getUnitLabel = @Composable { unit: DateUnit ->
        when (unit) {
            DateUnit.DAYS -> stringResource(id = R.string.unit_days)
            DateUnit.MONTHS -> stringResource(id = R.string.unit_months)
            DateUnit.YEARS -> stringResource(id = R.string.unit_years)
        }
    }

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
            label = { Text(stringResource(id = R.string.label_initial_date)) },
            enabled = false,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { datePickerDialog.show() },
            colors = OutlinedTextFieldDefaults.colors(
                disabledTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                disabledBorderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant
            )
        )

        OutlinedTextField(
            value = shiftAmount,
            onValueChange = {
                shiftAmount = it
                errorMessage = null
                resultDate = null
            },
            label = { Text(stringResource(id = R.string.label_shift_amount)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                unfocusedBorderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                focusedTextColor = MaterialTheme.colorScheme.primary,
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                focusedLabelColor = MaterialTheme.colorScheme.primary,
                cursorColor = MaterialTheme.colorScheme.primary,
                selectionColors = TextSelectionColors(
                    handleColor = MaterialTheme.colorScheme.primary,
                    backgroundColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
                )
            )
        )

        ExposedDropdownMenuBox(
            expanded = expandedUnitMenu,
            onExpandedChange = { expandedUnitMenu = !expandedUnitMenu }
        ) {
            OutlinedTextField(
                value = getUnitLabel(selectedUnit),
                onValueChange = {},
                readOnly = true,
                label = { Text(stringResource(id = R.string.label_unit)) },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedUnitMenu) },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unfocusedBorderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    focusedTextColor = MaterialTheme.colorScheme.primary,
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    focusedLabelColor = MaterialTheme.colorScheme.primary,
                )
            )
            ExposedDropdownMenu(
                expanded = expandedUnitMenu,
                onDismissRequest = { expandedUnitMenu = false },
                modifier = Modifier.background(MaterialTheme.colorScheme.surface)
            ) {
                DateUnit.entries.forEach { unit ->
                    DropdownMenuItem(
                        text = { Text(getUnitLabel(unit), color = MaterialTheme.colorScheme.onSurfaceVariant) },
                        onClick = {
                            selectedUnit = unit
                            expandedUnitMenu = false
                            resultDate = null
                        }
                    )
                }
            }
        }

        val errInvalidNumber = stringResource(id = R.string.err_invalid_number)
        val errShiftTooLarge = stringResource(id = R.string.err_shift_too_large)
        val errDateOutOfBounds = stringResource(id = R.string.err_date_out_of_bounds)

        Button(
            onClick = {
                val amount = shiftAmount.toLongOrNull()

                if (amount == null) {
                    errorMessage = errInvalidNumber
                    resultDate = null
                    return@Button
                }

                if (amount > 100000 || amount < -100000) {
                    errorMessage = errShiftTooLarge
                    resultDate = null
                    return@Button
                }

                errorMessage = null

                try {
                    resultDate = when (selectedUnit) {
                        DateUnit.DAYS -> startDate.plusDays(amount)
                        DateUnit.MONTHS -> startDate.plusMonths(amount)
                        DateUnit.YEARS -> startDate.plusYears(amount)
                    }
                } catch (e: Exception) {
                    errorMessage = errDateOutOfBounds
                    resultDate = null
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
            Text(stringResource(id = R.string.btn_calculate_shift), color = MaterialTheme.colorScheme.onPrimary)
        }

        errorMessage?.let {
            Text(text = it, color = MaterialTheme.colorScheme.error, fontWeight = FontWeight.Bold)
        }

        resultDate?.let {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(stringResource(id = R.string.title_new_date), color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(
                        text = it.format(dateFormatter),
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}