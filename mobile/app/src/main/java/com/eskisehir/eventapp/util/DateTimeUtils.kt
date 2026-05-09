package com.eskisehir.eventapp.util

import java.text.SimpleDateFormat
import java.util.*

object DateTimeUtils {
    /**
     * Formats an ISO date string (e.g., 2026-05-15T20:00) into a human-readable format.
     * Output example: "15 Mayıs 2026, 20:00"
     */
    fun formatEventDate(dateStr: String): String {
        return try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm", Locale.getDefault())
            val date = inputFormat.parse(dateStr) ?: return dateStr
            
            val outputFormat = SimpleDateFormat("dd MMMM yyyy, HH:mm", Locale("tr"))
            outputFormat.format(date)
        } catch (e: Exception) {
            dateStr
        }
    }

    /**
     * Short version for cards.
     * Output example: "15 Mayıs, 20:00"
     */
    fun formatEventDateShort(dateStr: String): String {
        return try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm", Locale.getDefault())
            val date = inputFormat.parse(dateStr) ?: return dateStr
            
            val outputFormat = SimpleDateFormat("dd MMMM, HH:mm", Locale("tr"))
            outputFormat.format(date)
        } catch (e: Exception) {
            dateStr
        }
    }

    /**
     * Extracts only the time from ISO date string.
     * Output example: "20:00"
     */
    fun formatEventTime(dateStr: String): String {
        return try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm", Locale.getDefault())
            val date = inputFormat.parse(dateStr) ?: return dateStr
            
            val outputFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
            outputFormat.format(date)
        } catch (e: Exception) {
            "00:00"
        }
    }
}
