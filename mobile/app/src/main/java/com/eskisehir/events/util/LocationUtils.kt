package com.eskisehir.events.util

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.DirectionsWalk
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.DirectionsTransit
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Utility functions for location and route formatting
 */
object LocationUtils {

    /**
     * Format duration in seconds to a human-readable string in Turkish
     * Example: 3660 seconds → "1 sa 1 dk"
     */
    fun formatDuration(seconds: Long): String {
        if (seconds <= 0) return "0 dk"

        val hours = seconds / 3600
        val minutes = (seconds % 3600) / 60

        return when {
            hours > 0 && minutes > 0 -> "$hours sa $minutes dk"
            hours > 0 -> "$hours sa"
            else -> "$minutes dk"
        }
    }

    /**
     * Format distance in meters to a human-readable string
     * Example: 4300 meters → "4.3 km"
     */
    fun formatDistance(meters: Long): String {
        if (meters <= 0) return "0 m"

        return when {
            meters >= 1000 -> {
                val km = meters / 1000.0
                String.format("%.1f km", km)
            }
            else -> "$meters m"
        }
    }

    /**
     * Get icon for travel mode
     */
    fun getTravelModeIcon(mode: String): ImageVector = when (mode) {
        "DRIVE" -> Icons.Default.DirectionsCar
        "WALK" -> Icons.AutoMirrored.Filled.DirectionsWalk
        "TRANSIT" -> Icons.Default.DirectionsTransit
        else -> Icons.Default.DirectionsCar
    }

    /**
     * Get Turkish label for travel mode
     */
    fun getTravelModeLabel(mode: String): String = when (mode) {
        "DRIVE" -> "Araba"
        "WALK" -> "Yürüyüş"
        "TRANSIT" -> "Toplu Taşıma"
        else -> mode
    }
}
