package com.eskisehir.events.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eskisehir.events.data.local.entity.RoadmapStopEntity
import com.eskisehir.events.data.repository.MapsRepository
import com.eskisehir.events.data.repository.RoadmapRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class RoadmapSegmentRoute(
    val fromIndex: Int,
    val toIndex: Int,
    val durationSeconds: Long = 0,
    val distanceMeters: Long = 0,
    val encodedPolyline: String = "",
    val isLoading: Boolean = true,
    val error: String? = null
)

data class RoadmapUiState(
    val stops: List<RoadmapStopEntity> = emptyList(),
    val segmentRoutes: Map<String, RoadmapSegmentRoute> = emptyMap(),
    val totalDurationSeconds: Long = 0,
    val totalDistanceMeters: Long = 0,
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class RoadmapViewModel @Inject constructor(
    private val roadmapRepository: RoadmapRepository,
    private val mapsRepository: MapsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(RoadmapUiState())
    val uiState: StateFlow<RoadmapUiState> = _uiState.asStateFlow()

    init {
        loadRoadmap()
    }

    /**
     * Load all roadmap stops and calculate routes between consecutive stops
     */
    private fun loadRoadmap() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            try {
                val stops = roadmapRepository.getAllStopsOnce()
                _uiState.value = _uiState.value.copy(stops = stops)

                // Calculate routes between consecutive stops
                if (stops.size >= 2) {
                    calculateSegmentRoutes(stops)
                }

                _uiState.value = _uiState.value.copy(isLoading = false)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Failed to load roadmap: ${e.message}"
                )
            }
        }
    }

    /**
     * Calculate routes between all consecutive stops
     */
    private suspend fun calculateSegmentRoutes(stops: List<RoadmapStopEntity>) {
        val segmentRoutes = mutableMapOf<String, RoadmapSegmentRoute>()
        var totalDuration = 0L
        var totalDistance = 0L

        for (i in 0 until stops.size - 1) {
            val fromStop = stops[i]
            val toStop = stops[i + 1]
            val key = "${i}_${i + 1}"

            // Initialize as loading
            segmentRoutes[key] = RoadmapSegmentRoute(
                fromIndex = i,
                toIndex = i + 1,
                isLoading = true
            )

            // Calculate route
            val result = mapsRepository.computeRoute(
                originLat = fromStop.latitude,
                originLng = fromStop.longitude,
                destinationLat = toStop.latitude,
                destinationLng = toStop.longitude,
                travelMode = "DRIVE"
            )

            result.onSuccess { routeData ->
                segmentRoutes[key] = RoadmapSegmentRoute(
                    fromIndex = i,
                    toIndex = i + 1,
                    durationSeconds = routeData.durationSeconds,
                    distanceMeters = routeData.distanceMeters,
                    encodedPolyline = routeData.encodedPolyline,
                    isLoading = false
                )
                totalDuration += routeData.durationSeconds
                totalDistance += routeData.distanceMeters
            }.onFailure { error ->
                segmentRoutes[key] = RoadmapSegmentRoute(
                    fromIndex = i,
                    toIndex = i + 1,
                    isLoading = false,
                    error = error.message ?: "Unknown error"
                )
            }

            _uiState.value = _uiState.value.copy(
                segmentRoutes = segmentRoutes,
                totalDurationSeconds = totalDuration,
                totalDistanceMeters = totalDistance
            )
        }
    }

    /**
     * Add a stop to the roadmap
     */
    fun addStop(
        eventId: Long,
        title: String,
        latitude: Double,
        longitude: Double,
        locationName: String,
        address: String
    ) {
        viewModelScope.launch {
            try {
                roadmapRepository.addStop(
                    eventId = eventId,
                    title = title,
                    latitude = latitude,
                    longitude = longitude,
                    locationName = locationName,
                    address = address
                )
                loadRoadmap()
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = "Failed to add stop: ${e.message}"
                )
            }
        }
    }

    /**
     * Remove a stop from the roadmap
     */
    fun removeStop(eventId: Long) {
        viewModelScope.launch {
            try {
                roadmapRepository.removeStop(eventId)
                loadRoadmap()
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = "Failed to remove stop: ${e.message}"
                )
            }
        }
    }

    /**
     * Reorder stops in the roadmap
     */
    fun reorderStops(newOrder: List<RoadmapStopEntity>) {
        viewModelScope.launch {
            try {
                roadmapRepository.reorderStops(newOrder)
                loadRoadmap()
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = "Failed to reorder stops: ${e.message}"
                )
            }
        }
    }

    /**
     * Clear the entire roadmap
     */
    fun clearRoadmap() {
        viewModelScope.launch {
            try {
                roadmapRepository.clearRoadmap()
                _uiState.value = RoadmapUiState()
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = "Failed to clear roadmap: ${e.message}"
                )
            }
        }
    }

    /**
     * Check if a specific event is in the roadmap
     */
    fun isEventInRoadmap(eventId: Long, callback: (Boolean) -> Unit) {
        viewModelScope.launch {
            try {
                val isInRoadmap = roadmapRepository.isEventInRoadmap(eventId)
                callback(isInRoadmap)
            } catch (e: Exception) {
                callback(false)
            }
        }
    }

    /**
     * Clear error message
     */
    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}
