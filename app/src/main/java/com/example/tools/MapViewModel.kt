package com.example.tools

import android.app.Application
import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.core.content.edit

class MapViewModel(application: Application) : AndroidViewModel(application) {
    private val prefs = application.getSharedPreferences("map_prefs", Context.MODE_PRIVATE)

    var latInput by mutableStateOf(prefs.getString("lat", "51.7592") ?: "51.7592")
        private set

    var lngInput by mutableStateOf(prefs.getString("lng", "19.4560") ?: "19.4560")
        private set

    fun updateLat(newLat: String) {
        if (newLat.isEmpty() || newLat.matches(Regex("^-?[0-9]*\\.?[0-9]*$"))) {
            latInput = newLat
            prefs.edit { putString("lat", latInput) }
        }
    }

    fun updateLng(newLng: String) {
        if (newLng.isEmpty() || newLng.matches(Regex("^-?[0-9]*\\.?[0-9]*$"))) {
            lngInput = newLng
            prefs.edit { putString("lng", lngInput) }
        }
    }
}