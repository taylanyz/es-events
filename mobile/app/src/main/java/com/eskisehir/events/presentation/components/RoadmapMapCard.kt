package com.eskisehir.events.presentation.components

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.eskisehir.eventapp.data.model.RoutePlaceSuggestion
import com.eskisehir.events.data.local.entity.RoadmapStopEntity
import com.eskisehir.events.util.PolylineDecoder
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState

/**
 * Displays all roadmap stops on a Google Map with polylines connecting them.
 * Fixed issue where markers wouldn't appear for newly added stops.
 */
@Composable
fun RoadmapMapCard(
    stops: List<RoadmapStopEntity>,
    suggestions: List<RoutePlaceSuggestion> = emptyList(),
    encodedPolylines: Map<String, String> = emptyMap(),
    isLoading: Boolean = false,
    focusedLocation: LatLng? = null,
    modifier: Modifier = Modifier
) {
    if (stops.isEmpty()) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .height(300.dp)
                .background(MaterialTheme.colorScheme.surfaceVariant),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "Rota için henüz durak eklenmedi.", color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        return
    }

    if (isLoading) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .height(300.dp)
                .background(MaterialTheme.colorScheme.surfaceVariant),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(39.7767, 30.5206), 13f)
    }

    // Focus on specific location if provided
    LaunchedEffect(focusedLocation) {
        focusedLocation?.let {
            cameraPositionState.animate(CameraUpdateFactory.newLatLngZoom(it, 16f))
        }
    }

    // Adjust camera to fit all stops and suggestions
    LaunchedEffect(stops) {
        if (stops.isNotEmpty() && focusedLocation == null) {
            try {
                val builder = LatLngBounds.Builder()
                var hasValidPoints = false
                
                stops.forEach { stop ->
                    if (isValidCoordinate(stop.latitude, stop.longitude)) {
                        builder.include(LatLng(stop.latitude, stop.longitude))
                        hasValidPoints = true
                    }
                }
                
                if (hasValidPoints) {
                    val bounds = builder.build()
                    cameraPositionState.animate(CameraUpdateFactory.newLatLngBounds(bounds, 180))
                }
            } catch (e: Exception) {
                Log.e("RoadmapDebug", "Camera bounds update failed: ${e.message}")
            }
        }
    }

    GoogleMap(
        modifier = modifier
            .fillMaxWidth()
            .height(300.dp),
        cameraPositionState = cameraPositionState
    ) {
        // 1. Draw polylines
        stops.indices.forEach { index ->
            if (index < stops.size - 1) {
                val key = "${index}_${index + 1}"
                val encodedPolyline = encodedPolylines[key]
                if (!encodedPolyline.isNullOrEmpty()) {
                    val points = remember(encodedPolyline) { PolylineDecoder.decode(encodedPolyline) }
                    Polyline(
                        points = points, 
                        color = MaterialTheme.colorScheme.primary, 
                        width = 12f,
                        jointType = com.google.android.gms.maps.model.JointType.ROUND
                    )
                }
            }
        }

        // 2. Draw markers for ALL roadmap stops with unique keys for stable rendering
        stops.forEachIndexed { index, stop ->
            key(stop.eventId) { // CRITICAL: Use key to ensure Marker re-renders for the specific stop
                val isAi = stop.isAiRecommended || stop.eventId < 0
                Marker(
                    state = rememberMarkerState(position = LatLng(stop.latitude, stop.longitude)),
                    title = "${index + 1}. ${stop.title}",
                    snippet = if (isAi) "Yol Üstü Önerilen Durak" else stop.locationName,
                    icon = BitmapDescriptorFactory.defaultMarker(
                        if (isAi) BitmapDescriptorFactory.HUE_ORANGE else BitmapDescriptorFactory.HUE_AZURE
                    )
                )
            }
        }

        // 3. Draw markers for floating suggestions (the ones NOT yet added to route)
        // These are shown in a lighter color or different style
        suggestions.forEach { suggestion ->
            val isAlreadyAdded = stops.any { it.title == suggestion.name }
            if (!isAlreadyAdded) {
                key(suggestion.id) {
                    Marker(
                        state = rememberMarkerState(position = LatLng(suggestion.latitude, suggestion.longitude)),
                        title = "Öneri: ${suggestion.name}",
                        snippet = "Mola noktası olarak ekleyebilirsiniz",
                        icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW),
                        alpha = 0.6f
                    )
                }
            }
        }
    }
}

private fun isValidCoordinate(lat: Double, lng: Double): Boolean {
    return lat in 38.5..41.0 && lng in 29.0..32.0
}
