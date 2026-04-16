@file:Suppress("COMPOSE_APPLIER_CALL_MISMATCH")

package com.example.tools

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen() {
    var latInput by remember { mutableStateOf("51.7592") }
    var lngInput by remember { mutableStateOf("19.4560") }

    val rawLat = latInput.toDoubleOrNull()
    val rawLng = lngInput.toDoubleOrNull()

    val displayLat = rawLat?.coerceIn(-85.0511, 85.0511)
    val displayLng = rawLng?.let { ((it % 360.0) + 540.0) % 360.0 - 180.0 }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(51.7592, 19.4560), 15f)
    }

    if (displayLat != null && displayLng != null) {
        LaunchedEffect(displayLat, displayLng) {
            val newLocation = LatLng(displayLat, displayLng)
            cameraPositionState.animate(CameraUpdateFactory.newLatLngZoom(newLocation, 15f))
        }
    }

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
                value = latInput,
                onValueChange = { input ->
                    if (input.isEmpty() || input.matches(Regex("^-?[0-9]*\\.?[0-9]*$"))) {
                        latInput = input
                    }
                },
                label = { Text(stringResource(id = R.string.label_latitude)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.weight(1f),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unfocusedBorderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    focusedTextColor = MaterialTheme.colorScheme.primary,
                    focusedBorderColor = MaterialTheme.colorScheme.primary
                )
            )

            OutlinedTextField(
                value = lngInput,
                onValueChange = { input ->
                    if (input.isEmpty() || input.matches(Regex("^-?[0-9]*\\.?[0-9]*$"))) {
                        lngInput = input
                    }
                },
                label = { Text(stringResource(id = R.string.label_longitude)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.weight(1f),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unfocusedBorderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    focusedTextColor = MaterialTheme.colorScheme.primary,
                    focusedBorderColor = MaterialTheme.colorScheme.primary
                )
            )
        }

        if (displayLat != null && displayLng != null) {
            val formatLat = String.format(Locale.US, "%.4f", displayLat)
            val formatLng = String.format(Locale.US, "%.4f", displayLng)

            Text(
                text = stringResource(id = R.string.msg_normalized_position, formatLat, formatLng),
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.labelMedium
            )
        }

        Card(
            modifier = Modifier.fillMaxSize(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            if (displayLat != null && displayLng != null) {
                GoogleMap(
                    modifier = Modifier.fillMaxSize(),
                    cameraPositionState = cameraPositionState,
                    uiSettings = MapUiSettings(zoomControlsEnabled = true)
                ) {
                    Marker(
                        state = MarkerState(position = LatLng(displayLat, displayLng)),
                        title = stringResource(id = R.string.marker_title_normalized),
                        snippet = stringResource(id = R.string.marker_snippet_coords, displayLat.toString(), displayLng.toString())
                    )
                }
            } else {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(stringResource(id = R.string.msg_enter_numbers_map), color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
    }
}