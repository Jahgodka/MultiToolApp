package com.example.tools

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NumberConverterScreen() {
    var inputValue by remember { mutableStateOf("") }

    val formats = listOf(
        "Binarny (Podstawa 2)",
        "Ósemkowy (Podstawa 8)",
        "Szesnastkowy (Podstawa 16)",
        "Base36 (Podstawa 36)"
    )
    var selectedFormat by remember { mutableStateOf(formats[0]) }

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
            label = { Text("Wpisz liczbę dziesiętną") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedTextColor = colorResource(id = R.color.bardzoJasnySzary),
                unfocusedBorderColor = colorResource(id = R.color.bardzoJasnySzary),
                unfocusedLabelColor = colorResource(id = R.color.bardzoJasnySzary),
                focusedTextColor = colorResource(id = R.color.jasnyNiebieski),
                focusedBorderColor = colorResource(id = R.color.jasnyNiebieski),
                focusedLabelColor = colorResource(id = R.color.jasnyNiebieski),
                cursorColor = colorResource(id = R.color.jasnyNiebieski)
            )
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .selectableGroup(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text("Wybierz format wyjściowy:", color = colorResource(id = R.color.bardzoJasnySzary))

            formats.forEach { format ->
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
                            selectedColor = colorResource(id = R.color.jasnyNiebieski),
                            unselectedColor = colorResource(id = R.color.bardzoJasnySzary)
                        )
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = format, color = colorResource(id = R.color.bardzoJasnySzary))
                }
            }
        }

        val number = inputValue.toLongOrNull()
        val limit = 2147483647L

        val (resultText, resultColor) = when {
            inputValue.isEmpty() -> Pair("Brak danych wejściowych", colorResource(id = R.color.bardzoJasnySzary))
            number == null -> Pair("Błąd: Nieprawidłowy format liczby", colorResource(id = R.color.czerwonyGlowny))
            number > limit || number < -limit -> Pair("Błąd: Przekroczono limit (+/- 2 147 483 647)", colorResource(id = R.color.czerwonyGlowny))
            else -> {
                val converted = when (selectedFormat) {
                    formats[0] -> number.toString(2)
                    formats[1] -> number.toString(8)
                    formats[2] -> number.toString(16).uppercase()
                    formats[3] -> number.toString(36).uppercase()
                    else -> ""
                }
                Pair(converted, colorResource(id = R.color.jasnyNiebieski))
            }
        }

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
                Text("Wynik:", color = colorResource(id = R.color.bardzoJasnySzary))
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