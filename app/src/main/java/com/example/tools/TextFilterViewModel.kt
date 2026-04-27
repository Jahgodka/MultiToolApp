package com.example.tools

import android.app.Application
import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.core.content.edit

class TextFilterViewModel(application: Application) : AndroidViewModel(application) {
    private val prefs = application.getSharedPreferences("text_filter_prefs", Context.MODE_PRIVATE)

    var inputText by mutableStateOf(prefs.getString("input_text", "") ?: "")
        private set

    var blockUppercase by mutableStateOf(prefs.getBoolean("block_upper", false))
        private set

    var blockLowercase by mutableStateOf(prefs.getBoolean("block_lower", false))
        private set

    var blockSpecialChars by mutableStateOf(prefs.getBoolean("block_special", false))
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    private fun saveState() {
        prefs.edit {
            putString("input_text", inputText)
                .putBoolean("block_upper", blockUppercase)
                .putBoolean("block_lower", blockLowercase)
                .putBoolean("block_special", blockSpecialChars)
        }
    }

    fun onInputChanged(newText: String, errUpper: String, errLower: String, errSpecial: String) {
        when {
            blockUppercase && newText.any { it.isUpperCase() } -> errorMessage = errUpper
            blockLowercase && newText.any { it.isLowerCase() } -> errorMessage = errLower
            blockSpecialChars && newText.any { !it.isLetterOrDigit() } -> errorMessage = errSpecial
            else -> {
                if (inputText != newText) {
                    inputText = newText
                    errorMessage = null
                    saveState()
                }
            }
        }
    }

    fun toggleUppercase(block: Boolean, errMessage: String) {
        blockUppercase = block
        if (block) {
            val filtered = inputText.filter { !it.isUpperCase() }
            if (filtered != inputText) {
                inputText = filtered
                errorMessage = errMessage
            } else {
                errorMessage = null
            }
        } else {
            errorMessage = null
        }
        saveState()
    }

    fun toggleLowercase(block: Boolean, errMessage: String) {
        blockLowercase = block
        if (block) {
            val filtered = inputText.filter { !it.isLowerCase() }
            if (filtered != inputText) {
                inputText = filtered
                errorMessage = errMessage
            } else {
                errorMessage = null
            }
        } else {
            errorMessage = null
        }
        saveState()
    }

    fun toggleSpecialChars(block: Boolean, errMessage: String) {
        blockSpecialChars = block
        if (block) {
            val filtered = inputText.filter { it.isLetterOrDigit() }
            if (filtered != inputText) {
                inputText = filtered
                errorMessage = errMessage
            } else {
                errorMessage = null
            }
        } else {
            errorMessage = null
        }
        saveState()
    }
}