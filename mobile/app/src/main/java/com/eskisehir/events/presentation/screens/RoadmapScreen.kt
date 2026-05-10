package com.eskisehir.events.presentation.screens

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.LocalCafe
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Save
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
import com.eskisehir.events.presentation.components.RoutePlaceSuggestionCard
import com.eskisehir.events.presentation.viewmodel.RoadmapViewModel
import com.eskisehir.events.presentation.components.TravelModeSelector
import com.eskisehir.events.util.LocationUtils
import com.eskisehir.eventapp.util.DateTimeUtils
import com.google.android.gms.maps.model.LatLng

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoadmapScreen(
    onBackClick: () -> Unit = {},
    viewModel: RoadmapViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    var focusedLocation by remember { mutableStateOf<LatLng?>(null) }
    var showSaveDialog by remember { mutableStateOf(false) }
    var routeTitle by remember { mutableStateOf("") }

    // Extract Roadmap Date
    val roadmapDate = remember(uiState.stops) {
        if (uiState.stops.isNotEmpty()) {
            DateTimeUtils.formatEventDate(uiState.stops.first().date).split(",").first()
        } else null
    }

    LaunchedEffect(uiState.saveSuccess) {
        if (uiState.saveSuccess) {
            Toast.makeText(context, "Rota kaydedildi!", Toast.LENGTH_SHORT).show()
            viewModel.clearError()
        }
    }

    LaunchedEffect(uiState.error) {
        uiState.error?.let {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            viewModel.clearError()
        }
    }

    // Combine all suggestions for map display
    val allSuggestions = remember(uiState.segmentRoutes) {
        uiState.segmentRoutes.values.flatMap { it.suggestions }
    }

    if (showSaveDialog) {
        AlertDialog(
            onDismissRequest = { showSaveDialog = false },
            title = { Text("Rotayı Kaydet") },
            text = {
                OutlinedTextField(
                    value = routeTitle,
                    onValueChange = { routeTitle = it },
                    label = { Text("Rota Adı") },
                    placeholder = { Text("Örn: Hafta Sonu Planım") },
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                Button(onClick = {
                    viewModel.saveCurrentRoute(routeTitle)
                    showSaveDialog = false
                    routeTitle = ""
                }) {
                    Text("Kaydet")
                }
            },
            dismissButton = {
                TextButton(onClick = { showSaveDialog = false }) {
                    Text("Vazgeç")
                }
            }
        )
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
                        IconButton(onClick = { 
                            if (uiState.stops.size < 2) {
                                Toast.makeText(context, "Rotayı kaydetmek için en az 2 durak eklemelisiniz.", Toast.LENGTH_SHORT).show()
                            } else {
                                showSaveDialog = true 
                            }
                        }) {
                            Icon(Icons.Default.Save, "Kaydet", tint = MaterialTheme.colorScheme.primary)
                        }
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
                        suggestions = allSuggestions,
                        encodedPolylines = uiState.segmentRoutes.mapValues { it.value.encodedPolyline },
                        isLoading = false,
                        focusedLocation = focusedLocation,
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
                        "Duraklar ve Öneriler",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(16.dp)
                    )
                }

                itemsIndexed(uiState.stops) { index, stop ->
                    // 1. Current Stop
                    RoadmapStopCard(
                        stop = stop,
                        index = index,
                        isFirst = index == 0,
                        isLast = index == uiState.stops.size - 1,
                        onRemove = { viewModel.removeStop(it) },
                        onMoveUp = { viewModel.moveStopUp(index) },
                        onMoveDown = { viewModel.moveStopDown(index) }
                    )
                    
                    // 2. Segment and Suggestions between this and next stop
                    if (index < uiState.stops.size - 1) {
                        val key = "${index}_${index + 1}"
                        val segment = uiState.segmentRoutes[key]
                        
                        if (segment != null) {
                            RoadmapSegmentCard(
                                fromStopName = uiState.stops[index].title,
                                toStopName = uiState.stops[index+1].title,
                                segmentRoute = segment
                            )

                            // SUGGESTIONS SECTION
                            SuggestionsSection(
                                segmentKey = key,
                                fromName = uiState.stops[index].title,
                                toName = uiState.stops[index+1].title,
                                segmentRoute = segment,
                                currentStops = uiState.stops,
                                onFetchClick = { viewModel.fetchSuggestionsForSegment(key) },
                                onShowOnMap = { focusedLocation = LatLng(it.latitude, it.longitude) },
                                onAddToRoute = { suggestion -> 
                                    viewModel.addSuggestionToRoute(suggestion, index + 1)
                                }
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
fun SuggestionsSection(
    segmentKey: String,
    fromName: String,
    toName: String,
    segmentRoute: com.eskisehir.events.presentation.viewmodel.RoadmapSegmentRoute,
    currentStops: List<com.eskisehir.events.data.local.entity.RoadmapStopEntity>,
    onFetchClick: () -> Unit,
    onShowOnMap: (com.eskisehir.eventapp.data.model.RoutePlaceSuggestion) -> Unit,
    onAddToRoute: (com.eskisehir.eventapp.data.model.RoutePlaceSuggestion) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        if (segmentRoute.suggestions.isEmpty() && !segmentRoute.isSuggestionsLoading) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .clickable { onFetchClick() },
                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.2f))
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        Icons.Default.Restaurant, 
                        null, 
                        modifier = Modifier.size(20.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(Modifier.width(12.dp))
                    Text(
                        "Yol Üstünde Mola Noktaları Keşfet", 
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        } else {
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.2f),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Default.LocalCafe, 
                                null, 
                                tint = MaterialTheme.colorScheme.secondary, 
                                modifier = Modifier.size(22.dp)
                            )
                            Spacer(Modifier.width(10.dp))
                            Text(
                                "Yol Üstünde Uğrayabilirsin",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.ExtraBold,
                                color = MaterialTheme.colorScheme.secondary
                            )
                        }
                        Text(
                            "Bu rota üzerindeki en iyi mola noktalarını senin için seçtik.",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.outline
                        )
                    }
                }
                
                if (segmentRoute.isSuggestionsLoading) {
                    LinearProgressIndicator(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
                        color = MaterialTheme.colorScheme.secondary,
                        trackColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.1f)
                    )
                } else {
                    Spacer(Modifier.height(8.dp))
                    segmentRoute.suggestions.forEach { suggestion ->
                        val isAlreadyInRoute = currentStops.any { it.title == suggestion.name }
                        RoutePlaceSuggestionCard(
                            suggestion = suggestion,
                            isAlreadyInRoute = isAlreadyInRoute,
                            onShowOnMap = { onShowOnMap(suggestion) },
                            onAddToRoute = { onAddToRoute(suggestion) }
                        )
                    }
                    Spacer(Modifier.height(8.dp))
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
