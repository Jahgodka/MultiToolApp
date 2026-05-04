package com.example.tools

import android.app.Application
import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel

class PasswordViewModel(application: Application) : AndroidViewModel(application) {
    private val prefs = application.getSharedPreferences("password_prefs", Context.MODE_PRIVATE)

    var passwordInput by mutableStateOf("")
        private set

    var minLength by mutableIntStateOf(prefs.getInt("min_length", 8))
        private set
    var minDigits by mutableIntStateOf(prefs.getInt("min_digits", 2))
        private set
    var minSpecial by mutableIntStateOf(prefs.getInt("min_special", 1))
        private set

    var isDialogOpen by mutableStateOf(false)
        private set

    var tempMinLength by mutableIntStateOf(minLength)
    var tempMinDigits by mutableIntStateOf(minDigits)
    var tempMinSpecial by mutableIntStateOf(minSpecial)

    fun updatePassword(newVal: String) {
        passwordInput = newVal
    }

    fun openDialog() {
        tempMinLength = minLength
        tempMinDigits = minDigits
        tempMinSpecial = minSpecial
        isDialogOpen = true
    }

    fun closeDialog() {
        isDialogOpen = false
    }

    fun saveLimits() {
        minLength = tempMinLength
        minDigits = tempMinDigits
        minSpecial = tempMinSpecial

        prefs.edit()
            .putInt("min_length", minLength)
            .putInt("min_digits", minDigits)
            .putInt("min_special", minSpecial)
            .apply()

        isDialogOpen = false
    }

    fun isLengthMet(): Boolean = passwordInput.length >= minLength
    fun isDigitsMet(): Boolean = passwordInput.count { it.isDigit() } >= minDigits
    fun isSpecialMet(): Boolean = passwordInput.count { !it.isLetterOrDigit() && !it.isWhitespace() } >= minSpecial
}