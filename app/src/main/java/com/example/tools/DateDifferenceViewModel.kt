package com.example.tools

import android.app.Application
import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import androidx.core.content.edit

class DateDifferenceViewModel(application: Application) : AndroidViewModel(application) {
    private val prefs = application.getSharedPreferences("date_diff_prefs", Context.MODE_PRIVATE)

    var startDate by mutableStateOf(parseDate(prefs.getString("start_date", "")) ?: LocalDate.now())
        private set
    var startTime by mutableStateOf(parseTime(prefs.getString("start_time", "")) ?: LocalTime.now())
        private set
    var endDate by mutableStateOf(parseDate(prefs.getString("end_date", "")) ?: LocalDate.now().plusDays(1))
        private set
    var endTime by mutableStateOf(parseTime(prefs.getString("end_time", "")) ?: LocalTime.of(12, 0))
        private set

    var resultDuration by mutableStateOf<Duration?>(null)
        private set

    private fun parseDate(dateStr: String?): LocalDate? {
        return try { if (!dateStr.isNullOrEmpty()) LocalDate.parse(dateStr) else null } catch (e: Exception) { null }
    }

    private fun parseTime(timeStr: String?): LocalTime? {
        return try { if (!timeStr.isNullOrEmpty()) LocalTime.parse(timeStr) else null } catch (e: Exception) { null }
    }

    private fun saveState() {
        prefs.edit {
            putString("start_date", startDate.toString())
                .putString("start_time", startTime.toString())
                .putString("end_date", endDate.toString())
                .putString("end_time", endTime.toString())
        }
    }

    fun updateStartDate(date: LocalDate) {
        startDate = date
        resultDuration = null
        saveState()
    }

    fun updateStartTime(time: LocalTime) {
        startTime = time
        resultDuration = null
        saveState()
    }

    fun updateEndDate(date: LocalDate) {
        endDate = date
        resultDuration = null
        saveState()
    }

    fun updateEndTime(time: LocalTime) {
        endTime = time
        resultDuration = null
        saveState()
    }

    fun resetToNowStart() {
        startDate = LocalDate.now()
        startTime = LocalTime.now()
        resultDuration = null
        saveState()
    }

    fun resetToNowEnd() {
        endDate = LocalDate.now()
        endTime = LocalTime.now()
        resultDuration = null
        saveState()
    }

    fun calculateDifference() {
        val startDateTime = LocalDateTime.of(startDate, startTime)
        val endDateTime = LocalDateTime.of(endDate, endTime)
        resultDuration = Duration.between(startDateTime, endDateTime).abs()
    }
}