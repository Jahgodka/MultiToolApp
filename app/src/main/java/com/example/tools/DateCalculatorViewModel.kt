package com.example.tools

import android.app.Application
import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import java.time.LocalDate
import androidx.core.content.edit

enum class DateUnit { DAYS, MONTHS, YEARS }

class DateCalculatorViewModel(application: Application) : AndroidViewModel(application) {

    private val prefs = application.getSharedPreferences("date_calc_prefs", Context.MODE_PRIVATE)

    var startDate by mutableStateOf(LocalDate.now())
        private set

    var shiftAmount by mutableStateOf("")
        private set

    var selectedUnit by mutableStateOf(DateUnit.DAYS)
        private set

    var resultDate by mutableStateOf<LocalDate?>(null)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    init {
        loadState()
    }

    private fun saveState() {
        prefs.edit {
            putLong("start_date", startDate.toEpochDay())
                .putString("shift_amount", shiftAmount)
                .putString("selected_unit", selectedUnit.name)
        }
    }

    private fun loadState() {
        val savedDate = prefs.getLong("start_date", LocalDate.now().toEpochDay())
        startDate = LocalDate.ofEpochDay(savedDate)
        shiftAmount = prefs.getString("shift_amount", "") ?: ""
        val savedUnit = prefs.getString("selected_unit", DateUnit.DAYS.name)
        selectedUnit = try {
            DateUnit.valueOf(savedUnit ?: DateUnit.DAYS.name)
        } catch (e: Exception) {
            DateUnit.DAYS
        }
    }

    fun onDateChanged(newDate: LocalDate) {
        startDate = newDate
        resultDate = null
        errorMessage = null
        saveState()
    }

    fun onAmountChanged(newAmount: String) {
        shiftAmount = newAmount
        errorMessage = null
        resultDate = null
        saveState()
    }

    fun onUnitChanged(newUnit: DateUnit) {
        selectedUnit = newUnit
        resultDate = null
        saveState()
    }

    fun calculate(errInvalid: String, errTooLarge: String, errOutOfBounds: String) {
        val amount = shiftAmount.toLongOrNull()

        if (amount == null) {
            errorMessage = errInvalid
            resultDate = null
            return
        }

        if (amount > 100000 || amount < -100000) {
            errorMessage = errTooLarge
            resultDate = null
            return
        }

        errorMessage = null

        try {
            resultDate = when (selectedUnit) {
                DateUnit.DAYS -> startDate.plusDays(amount)
                DateUnit.MONTHS -> startDate.plusMonths(amount)
                DateUnit.YEARS -> startDate.plusYears(amount)
            }
        } catch (e: Exception) {
            errorMessage = errOutOfBounds
            resultDate = null
        }
    }
}