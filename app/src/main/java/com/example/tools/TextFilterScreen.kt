package com.example.tools

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun TextFilterScreen(
    viewModel: TextFilterViewModel = viewModel()
) {
    val errUpper = stringResource(id = R.string.err_upper_blocked)
    val errLower = stringResource(id = R.string.err_lower_blocked)
    val errSpecial = stringResource(id = R.string.err_special_blocked)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        OutlinedTextField(
            value = viewModel.inputText,
            onValueChange = { viewModel.onInputChanged(it, errUpper, errLower, errSpecial) },
            label = { Text(stringResource(id = R.string.label_text_input)) },
            modifier = Modifier.fillMaxWidth(),
            isError = viewModel.errorMessage != null,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.onSurfaceVariant
            )
        )

        Box(modifier = Modifier.height(24.dp)) {
            viewModel.errorMessage?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(modifier = Modifier.padding(vertical = 8.dp)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { viewModel.toggleUppercase(!viewModel.blockUppercase, errUpper) }
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(stringResource(id = R.string.label_block_upper), color = MaterialTheme.colorScheme.onSurface)
                    Switch(
                        checked = viewModel.blockUppercase,
                        onCheckedChange = { viewModel.toggleUppercase(it, errUpper) }
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { viewModel.toggleLowercase(!viewModel.blockLowercase, errLower) }
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(stringResource(id = R.string.label_block_lower), color = MaterialTheme.colorScheme.onSurface)
                    Switch(
                        checked = viewModel.blockLowercase,
                        onCheckedChange = { viewModel.toggleLowercase(it, errLower) }
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { viewModel.toggleSpecialChars(!viewModel.blockSpecialChars, errSpecial) }
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(stringResource(id = R.string.label_block_special), color = MaterialTheme.colorScheme.onSurface)
                    Switch(
                        checked = viewModel.blockSpecialChars,
                        onCheckedChange = { viewModel.toggleSpecialChars(it, errSpecial) }
                    )
                }
            }
        }
    }
}