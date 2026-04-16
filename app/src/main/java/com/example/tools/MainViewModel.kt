package com.example.tools

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.core.content.edit

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val prefs = application.getSharedPreferences("app_settings", Context.MODE_PRIVATE)

    var currentThemeMode by mutableIntStateOf(
        prefs.getInt("theme_mode", AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
    )
        private set

    init {
        AppCompatDelegate.setDefaultNightMode(currentThemeMode)
    }

    fun updateTheme(mode: Int) {
        currentThemeMode = mode
        AppCompatDelegate.setDefaultNightMode(mode)

        prefs.edit { putInt("theme_mode", mode) }
    }
}