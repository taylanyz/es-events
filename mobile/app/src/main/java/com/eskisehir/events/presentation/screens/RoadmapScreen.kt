package com.eskisehir.events.presentation.screens

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.eskisehir.events.presentation.components.RoadmapMapCard
import com.eskisehir.events.presentation.components.RoadmapSegmentCard
import com.eskisehir.events.presentation.components.RoadmapStopCard
import com.eskisehir.events.presentation.viewmodel.RoadmapViewModel
import com.eskisehir.events.presentation.components.TravelModeSelector
import com.eskisehir.events.util.LocationUtils
import com.eskisehir.eventapp.util.DateTimeUtils

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoadmapScreen(
    onBackClick: () -> Unit = {},
    viewModel: RoadmapViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    // Extract Roadmap Date
    val roadmapDate = remember(uiState.stops) {
        if (uiState.stops.isNotEmpty()) {
            DateTimeUtils.formatEventDate(uiState.stops.first().date).split(",").first()
        } else null
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { 
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Rota Planım", fontWeight = FontWeight.Bold)
                        if (roadmapDate != null) {
                            Text(
                                text = roadmapDate,
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Geri")
                    }
                },
                actions = {
                    if (uiState.stops.isNotEmpty()) {
                        IconButton(onClick = { viewModel.clearRoadmap() }) {
                            Icon(Icons.Default.Delete, "Tümünü Temizle", tint = MaterialTheme.colorScheme.error)
                        }
                    }
                }
            )
        }
    ) { padding ->
        if (uiState.stops.isEmpty()) {
            EmptyRoadmapState(padding)
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentPadding = PaddingValues(bottom = 32.dp)
            ) {
                item {
                    // Map Card
                    RoadmapMapCard(
                        stops = uiState.stops,
                        encodedPolylines = uiState.segmentRoutes.mapValues { it.value.encodedPolyline },
                        isLoading = false,
                        modifier = Modifier.fillMaxWidth().height(260.dp)
                    )
                    
                    if (uiState.stops.size < 2) {
                        Surface(
                            modifier = Modifier.fillMaxWidth().padding(16.dp),
                            color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Warning, null, tint = MaterialTheme.colorScheme.secondary, modifier = Modifier.size(16.dp))
                                Spacer(Modifier.width(8.dp))
                                Text("Rota oluşturmak için aynı güne ait en az 2 etkinlik ekleyin.", style = MaterialTheme.typography.bodySmall)
                            }
                        }
                    }

                    Spacer(Modifier.height(8.dp))
                    
                    // Travel Mode Selector
                    TravelModeSelector(
                        selectedMode = uiState.selectedTravelMode,
                        onModeSelected = { viewModel.setTravelMode(it) }
                    )
                }

                if (uiState.stops.size >= 2) {
                    item {
                        SummaryCard(
                            duration = uiState.totalDurationSeconds,
                            distance = uiState.totalDistanceMeters,
                            stopCount = uiState.stops.size
                        )
                    }
                }

                item {
                    Text(
                        "Duraklar",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(16.dp)
                    )
                }

                itemsIndexed(uiState.stops) { index, stop ->
                    RoadmapStopCard(
                        stop = stop,
                        index = index,
                        isFirst = index == 0,
                        isLast = index == uiState.stops.size - 1,
                        onRemove = { viewModel.removeStop(it) },
                        onMoveUp = { viewModel.moveStopUp(index) },
                        onMoveDown = { viewModel.moveStopDown(index) }
                    )
                    
                    if (index < uiState.stops.size - 1) {
                        val segment = uiState.segmentRoutes["${index}_${index + 1}"]
                        if (segment != null) {
                            RoadmapSegmentCard(
                                fromStopName = uiState.stops[index].title,
                                toStopName = uiState.stops[index+1].title,
                                segmentRoute = segment
                            )
                        }
                    }
                }

                if (uiState.stops.size >= 2) {
                    item {
                        Spacer(Modifier.height(24.dp))
                        Button(
                            onClick = { openRoadmapInGoogleMaps(context, uiState.stops) },
                            modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
                        ) {
                            Icon(Icons.Default.Map, null)
                            Spacer(Modifier.width(8.dp))
                            Text("Google Haritalar'da Başlat")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun EmptyRoadmapState(padding: PaddingValues) {
    Column(
        modifier = Modifier.fillMaxSize().padding(padding).padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Surface(
            color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f),
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier.size(120.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(Icons.Default.Explore, null, modifier = Modifier.size(60.dp), tint = MaterialTheme.colorScheme.primary)
            }
        }
        Spacer(Modifier.height(24.dp))
        Text("Henüz Durak Eklenmedi", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.ExtraBold)
        Spacer(Modifier.height(8.dp))
        Text(
            "Etkinlik detay sayfasından etkinlikleri Rota'na ekleyerek kendi gezi planını oluşturabilirsin. Rota yalnızca aynı gün olan etkinliklerle oluşturulur.",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun SummaryCard(duration: Long, distance: Long, stopCount: Int) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f))
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(20.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            SummaryItem("Toplam Süre", LocationUtils.formatDuration(duration))
            SummaryItem("Mesafe", LocationUtils.formatDistance(distance))
            SummaryItem("Duraklar", "$stopCount Adet")
        }
    }
}

@Composable
fun SummaryItem(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.outline)
        Text(value, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
    }
}

private fun openRoadmapInGoogleMaps(context: Context, stops: List<com.eskisehir.events.data.local.entity.RoadmapStopEntity>) {
    if (stops.isEmpty()) return
    
    val origin = stops.first()
    val destination = stops.last()
    val waypoints = if (stops.size > 2) {
        stops.subList(1, stops.size - 1).joinToString("|") { "${it.latitude},${it.longitude}" }
    } else ""

    val uriString = if (waypoints.isNotEmpty()) {
        "https://www.google.com/maps/dir/?api=1&origin=${origin.latitude},${origin.longitude}&destination=${destination.latitude},${destination.longitude}&waypoints=$waypoints&travelmode=driving"
    } else {
        "https://www.google.com/maps/dir/?api=1&origin=${origin.latitude},${origin.longitude}&destination=${destination.latitude},${destination.longitude}&travelmode=driving"
    }
    
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uriString))
    context.startActivity(intent)
}
