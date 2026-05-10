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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.eskisehir.eventapp.BuildConfig
import com.eskisehir.events.util.LocationUtils
import com.eskisehir.events.util.PolylineDecoder
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.RoundCap
import com.google.maps.android.compose.*

/**
 * Coordinate validator for Eskisehir region.
 */
fun isValidEskisehirCoordinate(latitude: Double?, longitude: Double?): Boolean {
    if (latitude == null || longitude == null) return false
    return latitude in 39.0..40.5 && longitude in 29.5..31.5
}

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
    
    // 1. Etkinlik konumunu doğrula ve LatLng oluştur (Sıralama: Latitude, Longitude)
    val eventLatLng = remember(latitude, longitude) {
        if (isValidEskisehirCoordinate(latitude, longitude)) {
            LatLng(latitude, longitude)
        } else {
            Log.w("MapsDebug", "Geçersiz koordinat ($latitude, $longitude), Eskişehir merkezi kullanılıyor.")
            LatLng(39.7767, 30.5206) // Fallback: Eskişehir Valiliği
        }
    }
    
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(eventLatLng, 15f)
    }

    // 2. Kamera Animasyonu - Okyanus kaymasını engelleyen mantık
    LaunchedEffect(userLocation, eventLatLng, encodedPolyline) {
        try {
            val isUserInEskisehir = userLocation != null && isValidEskisehirCoordinate(userLocation.latitude, userLocation.longitude)
            
            if (isUserInEskisehir && !encodedPolyline.isNullOrEmpty()) {
                // Eğer hem kullanıcı hem etkinlik Eskişehir'deyse ikisini de göster
                val bounds = LatLngBounds.builder()
                    .include(userLocation!!)
                    .include(eventLatLng)
                    .build()
                cameraPositionState.animate(CameraUpdateFactory.newLatLngBounds(bounds, 150))
                Log.d("MapsDebug", "Kamera hem kullanıcıyı hem etkinliği kapsıyor.")
            } else {
                // Kullanıcı çok uzaktaysa (örn: Amerika/Emulator) sadece etkinliğe odaklan
                cameraPositionState.animate(CameraUpdateFactory.newLatLngZoom(eventLatLng, 15f))
                Log.d("MapsDebug", "Kullanıcı Eskişehir dışında veya rota yok. Sadece etkinliğe odaklanıldı.")
            }
        } catch (e: Exception) {
            Log.e("MapsDebug", "Kamera animasyon hatası: ${e.message}")
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
                        isMyLocationEnabled = isValidEskisehirCoordinate(userLocation?.latitude, userLocation?.longitude),
                        mapType = MapType.NORMAL,
                        isTrafficEnabled = true
                    ),
                    uiSettings = MapUiSettings(
                        zoomControlsEnabled = false,
                        myLocationButtonEnabled = true,
                        compassEnabled = true
                    ),
                    onMapLoaded = {
                        Log.d("MapsDebug", "Harita başarıyla yüklendi: $title")
                    }
                ) {
                    Marker(
                        state = rememberMarkerState(position = eventLatLng),
                        title = locationName.ifBlank { title },
                        snippet = address
                    )
                    
                    if (!encodedPolyline.isNullOrEmpty()) {
                        val points = remember(encodedPolyline) { PolylineDecoder.decode(encodedPolyline) }
                        Polyline(
                            points = points,
                            color = MaterialTheme.colorScheme.primary,
                            width = 12f,
                            jointType = com.google.android.gms.maps.model.JointType.ROUND,
                            startCap = RoundCap(),
                            endCap = RoundCap()
                        )
                    }
                }

                // Geçersiz koordinat uyarısı
                if (!isValidEskisehirCoordinate(latitude, longitude)) {
                    Surface(
                        modifier = Modifier.align(Alignment.BottomCenter).padding(8.dp),
                        color = Color.Black.copy(alpha = 0.7f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            "Etkinlik konumu bulunamadı, Eskişehir merkezi gösteriliyor.",
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            color = Color.White,
                            style = MaterialTheme.typography.labelSmall
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
                            val uri = Uri.parse("google.navigation:q=${eventLatLng.latitude},${eventLatLng.longitude}")
                            val intent = Intent(Intent.ACTION_VIEW, uri).apply {
                                setPackage("com.google.android.apps.maps")
                            }
                            try {
                                context.startActivity(intent)
                            } catch (e: Exception) {
                                val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/maps/dir/?api=1&destination=${eventLatLng.latitude},${eventLatLng.longitude}"))
                                context.startActivity(webIntent)
                            }
                        },
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer
                        )
                    ) {
                        Icon(Icons.Default.Directions, "Yol Tarifi")
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
