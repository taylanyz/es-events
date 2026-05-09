package com.eskisehir.eventapp.util

/**
 * Utility functions for location and distance/duration formatting
 */
object LocationUtils {
    /**
     * Format duration in seconds to user-friendly string
     * 720s → "12 dk"
     * 3660s → "1 sa 1 dk"
     */
    fun formatDuration(seconds: Long): String {
        if (seconds <= 0) return "0 dk"

        val hours = seconds / 3600
        val minutes = (seconds % 3600) / 60

        return when {
            hours == 0L -> "$minutes dk"
            minutes == 0L -> "$hours sa"
            else -> "$hours sa $minutes dk"
        }
    }

    /**
     * Format distance in meters to user-friendly string
     * 850m → "850 m"
     * 4300m → "4.3 km"
     */
    fun formatDistance(meters: Long): String {
        return when {
            meters < 1000 -> "$meters m"
            else -> {
                val km = meters / 1000.0
                val formatted = "%.1f".format(km).trimEnd('0').trimEnd('.')
                "$formatted km"
            }
        }
    }

    /**
     * Get travel mode icon emoji
     */
    fun getTravelModeIcon(mode: String): String = when (mode.uppercase()) {
        "DRIVE" -> "🚗"
        "WALK" -> "🚶"
        "TRANSIT" -> "🚌"
        else -> "📍"
    }

    /**
     * Get travel mode Turkish name
     */
    fun getTravelModeLabel(mode: String): String = when (mode.uppercase()) {
        "DRIVE" -> "Arabayla"
        "WALK" -> "Yürüyerek"
        "TRANSIT" -> "Toplu taşıma"
        else -> "Rota"
    }
}
