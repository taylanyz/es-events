package com.eskisehir.events.presentation.components

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Directions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.eskisehir.events.util.LocationUtils
import com.eskisehir.events.util.PolylineDecoder
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.RoundCap
import com.google.maps.android.compose.*

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
    encodedPolyline: String? = null,
    userLocation: LatLng? = null
) {
    val context = LocalContext.current
    val eventLocation = remember(latitude, longitude) { LatLng(latitude, longitude) }
    
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(eventLocation, 15f)
    }

    LaunchedEffect(userLocation, eventLocation) {
        try {
            if (userLocation != null && encodedPolyline != null) {
                val bounds = LatLngBounds.builder()
                    .include(userLocation)
                    .include(eventLocation)
                    .build()
                cameraPositionState.animate(CameraUpdateFactory.newLatLngBounds(bounds, 120))
            } else {
                cameraPositionState.animate(CameraUpdateFactory.newLatLngZoom(eventLocation, 16f))
            }
        } catch (e: Exception) {
            Log.e("GOOGLE_MAPS", "Camera Error: ${e.message}")
        }
    }

    ElevatedCard(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column {
            Box(modifier = Modifier.fillMaxWidth().height(240.dp)) {
                GoogleMap(
                    modifier = Modifier.fillMaxSize(),
                    cameraPositionState = cameraPositionState,
                    properties = MapProperties(
                        isMyLocationEnabled = userLocation != null,
                        mapType = MapType.NORMAL,
                        isTrafficEnabled = true
                    ),
                    uiSettings = MapUiSettings(
                        zoomControlsEnabled = false,
                        myLocationButtonEnabled = true,
                        compassEnabled = true
                    )
                ) {
                    Marker(
                        state = rememberMarkerState(position = eventLocation),
                        title = title,
                        snippet = locationName
                    )
                    
                    if (!encodedPolyline.isNullOrEmpty()) {
                        val points = remember(encodedPolyline) { PolylineDecoder.decode(encodedPolyline) }
                        Polyline(
                            points = points,
                            color = MaterialTheme.colorScheme.primary,
                            width = 15f,
                            jointType = com.google.android.gms.maps.model.JointType.ROUND,
                            startCap = RoundCap(),
                            endCap = RoundCap()
                        )
                    }
                }
            }

            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = locationName,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.ExtraBold
                        )
                        Text(
                            text = address,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.outline,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    
                    IconButton(
                        onClick = {
                            val uri = Uri.parse("google.navigation:q=$latitude,$longitude")
                            val intent = Intent(Intent.ACTION_VIEW, uri).apply {
                                setPackage("com.google.android.apps.maps")
                            }
                            try {
                                context.startActivity(intent)
                            } catch (e: Exception) {
                                val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/maps/dir/?api=1&destination=$latitude,$longitude"))
                                context.startActivity(webIntent)
                            }
                        },
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer
                        )
                    ) {
                        Icon(Icons.Default.Directions, "Git")
                    }
                }

                if (duration != null && distance != null) {
                    Surface(
                        modifier = Modifier.padding(top = 12.dp),
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.08f),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                LocationUtils.getTravelModeIcon(selectedTravelMode ?: "DRIVE"),
                                null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(Modifier.width(8.dp))
                            Text(
                                text = "${LocationUtils.getTravelModeLabel(selectedTravelMode ?: "DRIVE")}: ${LocationUtils.formatDuration(duration)} • ${LocationUtils.formatDistance(distance)}",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }
        }
    }
}
