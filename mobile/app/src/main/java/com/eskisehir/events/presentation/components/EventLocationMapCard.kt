package com.eskisehir.events.presentation.components

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.eskisehir.events.util.LocationUtils
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState

/**
 * Displays an event location on Google Maps with marker and location details
 */
@Composable
fun EventLocationMapCard(
    title: String,
    latitude: Double,
    longitude: Double,
    locationName: String,
    address: String,
    modifier: Modifier = Modifier,
    selectedTravelMode: String? = null,
    duration: Long? = null,
    distance: Long? = null,
    userLocation: LatLng? = null
) {
    val context = LocalContext.current
    val eventLocation = LatLng(latitude, longitude)
    val markerState = rememberMarkerState(position = eventLocation)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(eventLocation, 15f)
    }

    ElevatedCard(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            // Map
            GoogleMap(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                cameraPositionState = cameraPositionState
            ) {
                Marker(
                    state = markerState,
                    title = title
                )
                // Show user location if available
                if (userLocation != null) {
                    val userMarkerState = rememberMarkerState(position = userLocation)
                    Marker(
                        state = userMarkerState,
                        title = "Konumunuz"
                    )
                }
            }

            // Location Details
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(bottom = 8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = "Location",
                                modifier = Modifier.padding(end = 4.dp),
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = locationName,
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Text(
                            text = address,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    // Open in Maps Button
                    IconButton(
                        onClick = {
                            openInGoogleMaps(context, latitude, longitude, title)
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = "Open in Google Maps",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                // Route Information
                if (selectedTravelMode != null && duration != null && distance != null) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 12.dp)
                            .background(
                                color = MaterialTheme.colorScheme.surfaceVariant,
                                shape = MaterialTheme.shapes.small
                            )
                            .padding(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = LocationUtils.getTravelModeIcon(selectedTravelMode),
                                contentDescription = null,
                                modifier = Modifier.padding(end = 4.dp),
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = "${LocationUtils.getTravelModeLabel(selectedTravelMode)} ile ${LocationUtils.formatDuration(duration)} • ${LocationUtils.formatDistance(distance)}",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * Opens the event location in Google Maps app
 */
fun openInGoogleMaps(context: Context, latitude: Double, longitude: Double, label: String = "") {
    val uri = if (label.isNotEmpty()) {
        Uri.parse("geo:$latitude,$longitude?q=$latitude,$longitude($label)")
    } else {
        Uri.parse("geo:$latitude,$longitude")
    }
    val intent = Intent(Intent.ACTION_VIEW, uri).apply {
        setPackage("com.google.android.apps.maps")
    }
    try {
        context.startActivity(intent)
    } catch (e: Exception) {
        // Fallback to web-based maps
        val webIntent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse("https://maps.google.com/maps?q=$latitude,$longitude")
        }
        context.startActivity(webIntent)
    }
}
