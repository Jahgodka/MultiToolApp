package com.example.tools

import android.app.Application
import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.core.content.edit

class WebViewModel(application: Application) : AndroidViewModel(application) {
    private val prefs = application.getSharedPreferences("web_prefs", Context.MODE_PRIVATE)

    var urlInput by mutableStateOf(prefs.getString("last_url", "https://www.google.com") ?: "https://www.google.com")
        private set

    fun updateUrl(newUrl: String) {
        urlInput = newUrl
    }

    fun getValidUrlAndSave(): String {
        var formatted = urlInput.trim()

        if (formatted.isNotEmpty() && !formatted.startsWith("http://") && !formatted.startsWith("https://")) {
            formatted = "https://$formatted"
        }

        urlInput = formatted
        prefs.edit {putString("last_url", formatted)}

        return formatted
    }
}