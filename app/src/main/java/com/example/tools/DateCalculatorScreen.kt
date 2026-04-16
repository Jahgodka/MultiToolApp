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
import androidx.lifecycle.viewmodel.compose.viewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun getUnitLabel(unit: DateUnit): String {
    return when (unit) {
        DateUnit.DAYS -> stringResource(id = R.string.unit_days)
        DateUnit.MONTHS -> stringResource(id = R.string.unit_months)
        DateUnit.YEARS -> stringResource(id = R.string.unit_years)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateCalculatorScreen(
    viewModel: DateCalculatorViewModel = viewModel()
) {
    val context = LocalContext.current
    val dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy G", Locale.getDefault())

    val datePickerDialog = DatePickerDialog(
        context,
        { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
            viewModel.onDateChanged(LocalDate.of(year, month + 1, dayOfMonth))
        },
        viewModel.startDate.year,
        viewModel.startDate.monthValue - 1,
        viewModel.startDate.dayOfMonth
    )

    var expandedUnitMenu by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        OutlinedTextField(
            value = viewModel.startDate.format(dateFormatter),
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
            value = viewModel.shiftAmount,
            onValueChange = { viewModel.onAmountChanged(it) },
            label = { Text(stringResource(id = R.string.label_shift_amount)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                unfocusedBorderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                focusedTextColor = MaterialTheme.colorScheme.primary,
                focusedBorderColor = MaterialTheme.colorScheme.primary,
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
                value = getUnitLabel(viewModel.selectedUnit),
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
                    focusedTextColor = MaterialTheme.colorScheme.primary,
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
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
                            viewModel.onUnitChanged(unit)
                            expandedUnitMenu = false
                        }
                    )
                }
            }
        }

        val errInvalid = stringResource(id = R.string.err_invalid_number)
        val errTooLarge = stringResource(id = R.string.err_shift_too_large)
        val errOutOfBounds = stringResource(id = R.string.err_date_out_of_bounds)

        Button(
            onClick = { viewModel.calculate(errInvalid, errTooLarge, errOutOfBounds) },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
            Text(stringResource(id = R.string.btn_calculate_shift), color = MaterialTheme.colorScheme.onPrimary)
        }

        viewModel.errorMessage?.let {
            Text(text = it, color = MaterialTheme.colorScheme.error, fontWeight = FontWeight.Bold)
        }

        viewModel.resultDate?.let {
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