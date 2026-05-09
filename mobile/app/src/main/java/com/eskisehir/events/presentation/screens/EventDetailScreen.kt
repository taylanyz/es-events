package com.eskisehir.events.presentation.screens

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.eskisehir.events.presentation.components.EventLocationMapCard
import com.eskisehir.events.presentation.components.LocationPermissionInfo
import com.eskisehir.events.presentation.components.RouteInfoCard
import com.eskisehir.events.presentation.components.TravelModeSelector
import com.eskisehir.events.presentation.components.getTravelModeIcon
import com.eskisehir.events.presentation.viewmodel.MapsViewModel
import com.eskisehir.events.presentation.viewmodel.RoadmapViewModel

/**
 * Detail screen for displaying event information and calculating routes
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventDetailScreen(
    eventId: Long,
    eventTitle: String,
    latitude: Double,
    longitude: Double,
    locationName: String,
    address: String,
    eventDate: String = "2026-05-09T20:00", // Default value for test/compatibility
    onBackClick: () -> Unit = {},
    mapsViewModel: MapsViewModel = hiltViewModel(),
    roadmapViewModel: RoadmapViewModel = hiltViewModel()
) {
    val mapsUiState by mapsViewModel.uiState.collectAsState()
    val roadmapUiState by roadmapViewModel.uiState.collectAsState()

    var selectedTravelMode by remember { mutableStateOf("DRIVE") }
    var isInRoadmap by remember { mutableStateOf(false) }

    // Check if event is in roadmap
    LaunchedEffect(eventId, roadmapUiState.stops) {
        isInRoadmap = roadmapUiState.stops.any { it.eventId == eventId }
    }

    // Permission launcher
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val granted = permissions.all { it.value }
        mapsViewModel.onPermissionStatusChanged(granted)
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(eventTitle) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    if (isInRoadmap) {
                        roadmapViewModel.removeStop(eventId)
                    } else {
                        roadmapViewModel.addStop(
                            eventId = eventId,
                            title = eventTitle,
                            latitude = latitude,
                            longitude = longitude,
                            locationName = locationName,
                            address = address,
                            date = eventDate
                        )
                    }
                }
            ) {
                Icon(
                    imageVector = if (isInRoadmap) Icons.Default.Close else Icons.Default.Add,
                    contentDescription = if (isInRoadmap) "Remove from Roadmap" else "Add to Roadmap"
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Top
        ) {
            // Event Location Card
            EventLocationMapCard(
                title = eventTitle,
                latitude = latitude,
                longitude = longitude,
                locationName = locationName,
                address = address
            )

            // Location Permission Info
            LocationPermissionInfo(
                isGranted = mapsUiState.locationPermissionGranted,
                error = mapsUiState.locationError,
                onRequestPermission = {
                    permissionLauncher.launch(
                        arrayOf(
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        )
                    )
                }
            )

            // Route Calculation Section (only if location permission granted)
            if (mapsUiState.locationPermissionGranted && mapsUiState.userLocation != null) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    Text(
                        text = "Route from Your Location",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )

                    // Travel Mode Selector
                    TravelModeSelector(
                        selectedMode = selectedTravelMode,
                        onModeSelected = { mode ->
                            selectedTravelMode = mode
                            // Calculate route for selected mode if not already done
                            mapsViewModel.calculateRoute(
                                originLat = mapsUiState.userLocation!!.latitude,
                                originLng = mapsUiState.userLocation!!.longitude,
                                destLat = latitude,
                                destLng = longitude,
                                travelMode = mode
                            )
                        }
                    )

                    // Route Info Cards
                    RouteInfoCard(
                        travelMode = "DRIVE",
                        routeState = mapsUiState.driveRoute,
                        routeIcon = getTravelModeIcon("DRIVE"),
                        isSelected = selectedTravelMode == "DRIVE",
                        onSelect = { selectedTravelMode = "DRIVE" }
                    )

                    RouteInfoCard(
                        travelMode = "WALK",
                        routeState = mapsUiState.walkRoute,
                        routeIcon = getTravelModeIcon("WALK"),
                        isSelected = selectedTravelMode == "WALK",
                        onSelect = { selectedTravelMode = "WALK" }
                    )

                    RouteInfoCard(
                        travelMode = "TRANSIT",
                        routeState = mapsUiState.transitRoute,
                        routeIcon = getTravelModeIcon("TRANSIT"),
                        isSelected = selectedTravelMode == "TRANSIT",
                        onSelect = { selectedTravelMode = "TRANSIT" }
                    )

                    // Calculate All Routes Button
                    Button(
                        onClick = {
                            mapsViewModel.calculateAllRoutes(
                                originLat = mapsUiState.userLocation!!.latitude,
                                originLng = mapsUiState.userLocation!!.longitude,
                                destLat = latitude,
                                destLng = longitude
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Text("Calculate All Routes")
                    }
                }
            } else if (!mapsUiState.locationPermissionGranted) {
                // Show message to request permission
                Text(
                    text = "Grant location permission to calculate routes",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Roadmap Status
            if (roadmapUiState.error != null) {
                Text(
                    text = roadmapUiState.error ?: "",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }

            // Bottom spacing
            Column(modifier = Modifier.height(80.dp)) {}
        }
    }
}
