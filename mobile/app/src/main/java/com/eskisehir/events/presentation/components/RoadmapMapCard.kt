package com.eskisehir.events.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.eskisehir.events.data.local.entity.RoadmapStopEntity
import com.eskisehir.events.util.PolylineDecoder
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState

/**
 * Displays all roadmap stops on a Google Map with polylines connecting them
 */
@Composable
fun RoadmapMapCard(
    stops: List<RoadmapStopEntity>,
    encodedPolylines: Map<String, String> = emptyMap(),
    isLoading: Boolean = false,
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
            Text(
                text = "No stops added to roadmap",
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
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

    // Calculate bounds for all stops
    val bounds = if (stops.isNotEmpty()) {
        val builder = LatLngBounds.Builder()
        stops.forEach { stop ->
            builder.include(LatLng(stop.latitude, stop.longitude))
        }
        builder.build()
    } else {
        null
    }

    val cameraPositionState = rememberCameraPositionState {
        if (bounds != null) {
            position = CameraPosition.fromLatLngZoom(
                bounds.center,
                13f
            )
        }
    }

    GoogleMap(
        modifier = modifier
            .fillMaxWidth()
            .height(300.dp),
        cameraPositionState = cameraPositionState
    ) {
        // Draw polylines between consecutive stops
        stops.indices.forEach { index ->
            if (index < stops.size - 1) {
                val key = "${index}_${index + 1}"
                val encodedPolyline = encodedPolylines[key]
                if (!encodedPolyline.isNullOrEmpty()) {
                    val points = PolylineDecoder.decode(encodedPolyline)
                    Polyline(
                        points = points,
                        color = MaterialTheme.colorScheme.primary,
                        width = 5f
                    )
                }
            }
        }

        // Draw markers for all stops
        stops.forEachIndexed { index, stop ->
            val markerState = rememberMarkerState(
                position = LatLng(stop.latitude, stop.longitude)
            )
            Marker(
                state = markerState,
                title = stop.title,
                snippet = stop.locationName,
                tag = index  // Use index as tag to identify stops
            )
        }
    }
}
