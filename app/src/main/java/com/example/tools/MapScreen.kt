package com.example.tools

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen() {
    var latInput by remember { mutableStateOf("51.7592") }
    var lngInput by remember { mutableStateOf("19.4560") }

    val lat = latInput.toDoubleOrNull()
    val lng = lngInput.toDoubleOrNull()

    val isLatValid = lat != null && lat in -90.0..90.0
    val isLngValid = lng != null && lng in -180.0..180.0

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(51.7592, 19.4560), 15f)
    }

    if (lat != null && lng != null && lat in -90.0..90.0 && lng in -180.0..180.0) {
        LaunchedEffect(lat, lng) {
            val newLocation = LatLng(lat, lng)
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
                label = { Text("Lat (Szerokość)") },
                isError = !isLatValid && latInput.isNotEmpty(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.weight(1f),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedTextColor = colorResource(id = R.color.bardzoJasnySzary),
                    unfocusedBorderColor = colorResource(id = R.color.bardzoJasnySzary),
                    focusedTextColor = colorResource(id = R.color.jasnyNiebieski),
                    focusedBorderColor = colorResource(id = R.color.jasnyNiebieski),
                    errorBorderColor = colorResource(id = R.color.czerwonyGlowny)
                )
            )

            OutlinedTextField(
                value = lngInput,
                onValueChange = { input ->
                    if (input.isEmpty() || input.matches(Regex("^-?[0-9]*\\.?[0-9]*$"))) {
                        lngInput = input
                    }
                },
                label = { Text("Lng (Długość)") },
                isError = !isLngValid && lngInput.isNotEmpty(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.weight(1f),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedTextColor = colorResource(id = R.color.bardzoJasnySzary),
                    unfocusedBorderColor = colorResource(id = R.color.bardzoJasnySzary),
                    focusedTextColor = colorResource(id = R.color.jasnyNiebieski),
                    focusedBorderColor = colorResource(id = R.color.jasnyNiebieski),
                    errorBorderColor = colorResource(id = R.color.czerwonyGlowny)
                )
            )
        }

        if (!isLatValid && latInput.isNotEmpty()) {
            Text("Błąd: Szerokość musi być w przedziale od -90 do 90.", color = colorResource(id = R.color.czerwonyGlowny), fontWeight = FontWeight.Bold)
        }
        if (!isLngValid && lngInput.isNotEmpty()) {
            Text("Błąd: Długość musi być w przedziale od -180 do 180.", color = colorResource(id = R.color.czerwonyGlowny), fontWeight = FontWeight.Bold)
        }

        Card(
            modifier = Modifier.fillMaxSize(),
            colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.szary))
        ) {
            if (isLatValid && isLngValid) {
                GoogleMap(
                    modifier = Modifier.fillMaxSize(),
                    cameraPositionState = cameraPositionState,
                    uiSettings = MapUiSettings(zoomControlsEnabled = true)
                ) {
                    Marker(
                        state = MarkerState(position = LatLng(lat, lng)),
                        title = "Wybrane koordynaty",
                        snippet = "Lat: $lat, Lng: $lng"
                    )
                }
            } else {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Wprowadź poprawne współrzędne, aby wyrenderować mapę.", color = colorResource(id = R.color.bardzoJasnySzary))
                }
            }
        }
    }
}