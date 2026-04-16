package com.example.tools

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

enum class NumberFormatType { BINARY, OCTAL, HEXADECIMAL, BASE36 }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NumberConverterScreen() {
    var inputValue by remember { mutableStateOf("") }
    var selectedFormat by remember { mutableStateOf(NumberFormatType.BINARY) }

    val getFormatLabel = @Composable { format: NumberFormatType ->
        when (format) {
            NumberFormatType.BINARY -> stringResource(id = R.string.format_binary)
            NumberFormatType.OCTAL -> stringResource(id = R.string.format_octal)
            NumberFormatType.HEXADECIMAL -> stringResource(id = R.string.format_hex)
            NumberFormatType.BASE36 -> stringResource(id = R.string.format_base36)
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
            value = inputValue,
            onValueChange = { input ->
                val strictRegex = Regex("^(0|-|-[1-9][0-9]{0,9}|[1-9][0-9]{0,9})$")

                if (input.isEmpty() || input.matches(strictRegex)) {
                    inputValue = input
                }
            },
            label = { Text(stringResource(id = R.string.label_enter_decimal)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                unfocusedBorderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                focusedTextColor = MaterialTheme.colorScheme.primary,
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                focusedLabelColor = MaterialTheme.colorScheme.primary,
                cursorColor = MaterialTheme.colorScheme.primary
            )
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .selectableGroup(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(stringResource(id = R.string.label_choose_format), color = MaterialTheme.colorScheme.onSurfaceVariant)

            NumberFormatType.entries.forEach { format ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .selectable(
                            selected = (format == selectedFormat),
                            onClick = { selectedFormat = format },
                            role = Role.RadioButton
                        )
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = (format == selectedFormat),
                        onClick = null,
                        colors = RadioButtonDefaults.colors(
                            selectedColor = MaterialTheme.colorScheme.primary,
                            unselectedColor = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = getFormatLabel(format), color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }

        val number = inputValue.toLongOrNull()
        val limit = 2147483647L

        val errNoInput = stringResource(id = R.string.err_no_input)
        val errInvalidFormat = stringResource(id = R.string.err_invalid_format)
        val errLimitExceeded = stringResource(id = R.string.err_limit_exceeded)

        val (resultText, resultColor) = when {
            inputValue.isEmpty() -> Pair(errNoInput, MaterialTheme.colorScheme.onSurfaceVariant)
            number == null -> Pair(errInvalidFormat, MaterialTheme.colorScheme.error)
            number > limit || number < -limit -> Pair(errLimitExceeded, MaterialTheme.colorScheme.error)
            else -> {
                val converted = when (selectedFormat) {
                    NumberFormatType.BINARY -> number.toString(2)
                    NumberFormatType.OCTAL -> number.toString(8)
                    NumberFormatType.HEXADECIMAL -> number.toString(16).uppercase()
                    NumberFormatType.BASE36 -> number.toString(36).uppercase()
                }
                Pair(converted, MaterialTheme.colorScheme.primary)
            }
        }

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
                Text(stringResource(id = R.string.title_result), color = MaterialTheme.colorScheme.onSurfaceVariant)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = resultText,
                    style = MaterialTheme.typography.headlineSmall,
                    color = resultColor,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}