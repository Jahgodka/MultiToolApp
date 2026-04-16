package com.example.tools

import android.app.Application
import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.core.content.edit

class NumberConverterViewModel(application: Application) : AndroidViewModel(application) {
    private val prefs = application.getSharedPreferences("number_prefs", Context.MODE_PRIVATE)

    var inputValue by mutableStateOf(prefs.getString("input", "") ?: "")
        private set

    var selectedFormat by mutableStateOf(
        NumberFormatType.valueOf(prefs.getString("format", NumberFormatType.BINARY.name) ?: NumberFormatType.BINARY.name)
    )
        private set

    fun updateInput(newInput: String) {
        val strictRegex = Regex("^(0|-|-[1-9][0-9]{0,9}|[1-9][0-9]{0,9})$")
        if (newInput.isEmpty() || newInput.matches(strictRegex)) {
            inputValue = newInput
            prefs.edit { putString("input", inputValue) }
        }
    }

    fun updateFormat(newFormat: NumberFormatType) {
        selectedFormat = newFormat
        prefs.edit { putString("format", selectedFormat.name) }
    }
}