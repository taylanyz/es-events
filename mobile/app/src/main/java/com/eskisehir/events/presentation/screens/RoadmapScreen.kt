package com.eskisehir.events.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.eskisehir.events.presentation.components.RoadmapMapCard
import com.eskisehir.events.presentation.components.RoadmapSegmentCard
import com.eskisehir.events.presentation.components.RoadmapStopCard
import com.eskisehir.events.presentation.viewmodel.RoadmapViewModel
import com.eskisehir.events.util.LocationUtils

/**
 * Screen for managing roadmap with multiple stops and routes
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoadmapScreen(
    onBackClick: () -> Unit = {},
    viewModel: RoadmapViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Journey Roadmap") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    if (uiState.stops.isNotEmpty()) {
                        IconButton(
                            onClick = { viewModel.clearRoadmap() }
                        ) {
                            Icon(Icons.Default.Delete, contentDescription = "Clear Roadmap")
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        if (uiState.stops.isEmpty()) {
            // Empty state
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "No Stops Added",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Add events from the detail screen to create a roadmap",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                verticalArrangement = Arrangement.Top
            ) {
                // Map showing all stops
                item {
                    RoadmapMapCard(
                        stops = uiState.stops,
                        encodedPolylines = uiState.segmentRoutes.mapValues { (_, route) ->
                            route.encodedPolyline
                        },
                        isLoading = uiState.isLoading,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    )
                }

                // Summary information
                item {
                    if (uiState.stops.size >= 2) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "Total Duration",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = LocationUtils.formatDuration(uiState.totalDurationSeconds),
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Column {
                                Text(
                                    text = "Total Distance",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = LocationUtils.formatDistance(uiState.totalDistanceMeters),
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Column {
                                Text(
                                    text = "Total Stops",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = uiState.stops.size.toString(),
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }

                // Stops list
                itemsIndexed(uiState.stops) { index, stop ->
                    RoadmapStopCard(
                        stop = stop,
                        index = index,
                        onRemove = { eventId ->
                            viewModel.removeStop(eventId)
                        }
                    )
                }

                // Segment routes
                itemsIndexed(
                    (0 until maxOf(0, uiState.stops.size - 1)).toList()
                ) { index, _ ->
                    val segmentRoute = uiState.segmentRoutes["${index}_${index + 1}"]
                    if (segmentRoute != null && index + 1 < uiState.stops.size) {
                        RoadmapSegmentCard(
                            fromStopName = uiState.stops[index].title,
                            toStopName = uiState.stops[index + 1].title,
                            segmentRoute = segmentRoute
                        )
                    }
                }

                // Error message
                if (uiState.error != null) {
                    item {
                        Text(
                            text = uiState.error ?: "",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                    }
                }

                // Bottom spacing
                item {
                    Column(modifier = Modifier.padding(bottom = 80.dp)) {}
                }
            }
        }
    }
}
