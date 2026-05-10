package com.eskisehir.eventapp.ui.screens.savedroutes

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Route
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.eskisehir.events.data.local.entity.SavedRouteEntity
import com.eskisehir.events.presentation.components.RoadmapMapCard
import com.eskisehir.events.presentation.viewmodel.RoadmapViewModel
import com.eskisehir.events.util.LocationUtils
import com.eskisehir.eventapp.ui.viewmodels.SavedRouteDetailViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SavedRouteDetailScreen(
    routeId: Long,
    onBackClick: () -> Unit,
    onOpenInRoadmap: () -> Unit,
    viewModel: SavedRouteDetailViewModel = hiltViewModel(),
    roadmapViewModel: RoadmapViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val route by viewModel.route.collectAsState()

    LaunchedEffect(routeId) {
        viewModel.loadRoute(routeId)
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(route?.title ?: "Rota Detayı", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Geri")
                    }
                },
                actions = {
                    route?.let { r ->
                        IconButton(onClick = {
                            viewModel.deleteRoute(r.id)
                            onBackClick()
                        }) {
                            Icon(Icons.Default.Delete, "Sil", tint = MaterialTheme.colorScheme.error)
                        }
                    }
                }
            )
        }
    ) { padding ->
        if (route == null) {
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            val r = route!!
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentPadding = PaddingValues(bottom = 32.dp)
            ) {
                item {
                    // Map Card
                    RoadmapMapCard(
                        stops = emptyList(), // Not used for this version if segments provided
                        encodedPolylines = r.segments.associate { "${it.fromIndex}_${it.toIndex}" to it.encodedPolyline },
                        modifier = Modifier.fillMaxWidth().height(260.dp)
                    )
                    
                    // Stats
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f))
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(20.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            DetailStatItem("Süre", LocationUtils.formatDuration(r.totalDurationSeconds))
                            DetailStatItem("Mesafe", LocationUtils.formatDistance(r.totalDistanceMeters))
                            DetailStatItem("Mod", LocationUtils.getTravelModeLabel(r.travelMode))
                        }
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

                itemsIndexed(r.stops) { index, stop ->
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 6.dp),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                color = MaterialTheme.colorScheme.primary,
                                shape = androidx.compose.foundation.shape.CircleShape,
                                modifier = Modifier.size(28.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text((index + 1).toString(), color = Color.White, style = MaterialTheme.typography.labelMedium)
                                }
                            }
                            Spacer(Modifier.width(16.dp))
                            Column {
                                Text(stop.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                                Text(stop.locationName, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.outline)
                            }
                        }
                    }
                }

                item {
                    Spacer(Modifier.height(24.dp))
                    Button(
                        onClick = {
                            roadmapViewModel.loadSavedRoute(r)
                            onOpenInRoadmap()
                        },
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.Default.Route, null)
                        Spacer(Modifier.width(8.dp))
                        Text("Roadmap'te Aç")
                    }
                    
                    OutlinedButton(
                        onClick = { openInGoogleMaps(context, r) },
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 8.dp),
                        shape = RoundedCornerShape(12.dp)
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

@Composable
fun DetailStatItem(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.outline)
        Text(value, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
    }
}

private fun openInGoogleMaps(context: android.content.Context, route: SavedRouteEntity) {
    if (route.stops.isEmpty()) return
    val origin = route.stops.first()
    val destination = route.stops.last()
    val waypoints = if (route.stops.size > 2) {
        route.stops.subList(1, route.stops.size - 1).joinToString("|") { "${it.latitude},${it.longitude}" }
    } else ""
    val uriString = "https://www.google.com/maps/dir/?api=1&origin=${origin.latitude},${origin.longitude}&destination=${destination.latitude},${destination.longitude}&waypoints=$waypoints&travelmode=driving"
    context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(uriString)))
}
