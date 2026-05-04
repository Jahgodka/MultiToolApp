package com.example.tools

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun PasswordScreen(
    viewModel: PasswordViewModel = viewModel()
) {
    // Stan UI - czy hasło jest aktualnie odkryte
    var isPasswordVisible by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = viewModel.passwordInput,
                onValueChange = { viewModel.updatePassword(it) },
                label = { Text(stringResource(id = R.string.label_password_input)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                // 1. Zabezpieczenie klawiatury (brak autokorekty)
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                // 2. Transformacja znaków na kropki w zależności od stanu
                visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                // 3. Ikona na końcu pola (oczko)
                trailingIcon = {
                    val image = if (isPasswordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                    val description = if (isPasswordVisible) "Ukryj hasło" else "Pokaż hasło"

                    IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                        Icon(imageVector = image, contentDescription = description)
                    }
                }
            )

            Button(
                onClick = { viewModel.openDialog() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(id = R.string.btn_configure))
            }

            // Karta z dynamiczną checklistą
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    RequirementRow(
                        label = "${stringResource(id = R.string.label_min_length)}: ${viewModel.minLength}",
                        isMet = viewModel.isLengthMet()
                    )
                    RequirementRow(
                        label = "${stringResource(id = R.string.label_min_digits)}: ${viewModel.minDigits}",
                        isMet = viewModel.isDigitsMet()
                    )
                    RequirementRow(
                        label = "${stringResource(id = R.string.label_min_special)}: ${viewModel.minSpecial}",
                        isMet = viewModel.isSpecialMet()
                    )
                }
            }
        }

        if (viewModel.isDialogOpen) {
            LimitsDialog(viewModel)
        }
    }
}

@Composable
fun RequirementRow(label: String, isMet: Boolean) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .size(24.dp)
                .background(
                    color = if (isMet) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error,
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = if (isMet) Icons.Default.Check else Icons.Default.Close,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.size(16.dp)
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = label,
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
fun LimitsDialog(viewModel: PasswordViewModel) {
    AlertDialog(
        onDismissRequest = { viewModel.closeDialog() },
        title = { Text(stringResource(id = R.string.dialog_title_limits)) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                StepperControl(
                    label = stringResource(id = R.string.label_min_length),
                    value = viewModel.tempMinLength,
                    onDecrement = { if (viewModel.tempMinLength > 4) viewModel.tempMinLength-- },
                    onIncrement = { if (viewModel.tempMinLength < 64) viewModel.tempMinLength++ }
                )
                StepperControl(
                    label = stringResource(id = R.string.label_min_digits),
                    value = viewModel.tempMinDigits,
                    onDecrement = { if (viewModel.tempMinDigits > 0) viewModel.tempMinDigits-- },
                    onIncrement = { if (viewModel.tempMinDigits < 16) viewModel.tempMinDigits++ }
                )
                StepperControl(
                    label = stringResource(id = R.string.label_min_special),
                    value = viewModel.tempMinSpecial,
                    onDecrement = { if (viewModel.tempMinSpecial > 0) viewModel.tempMinSpecial-- },
                    onIncrement = { if (viewModel.tempMinSpecial < 16) viewModel.tempMinSpecial++ }
                )
            }
        },
        confirmButton = {
            TextButton(onClick = { viewModel.saveLimits() }) {
                Text(stringResource(id = R.string.btn_save), fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = { viewModel.closeDialog() }) {
                Text(stringResource(id = R.string.btn_cancel))
            }
        }
    )
}

@Composable
fun StepperControl(
    label: String,
    value: Int,
    onDecrement: () -> Unit,
    onIncrement: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = label, modifier = Modifier.weight(1f))
        Row(verticalAlignment = Alignment.CenterVertically) {
            FilledTonalIconButton(onClick = onDecrement, modifier = Modifier.size(32.dp)) {
                Text("-", fontWeight = FontWeight.Bold)
            }
            Text(
                text = value.toString(),
                modifier = Modifier.padding(horizontal = 12.dp),
                fontWeight = FontWeight.Bold
            )
            FilledTonalIconButton(onClick = onIncrement, modifier = Modifier.size(32.dp)) {
                Text("+", fontWeight = FontWeight.Bold)
            }
        }
    }
}