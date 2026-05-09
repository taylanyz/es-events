package com.eskisehir.events.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eskisehir.events.data.local.entity.RoadmapStopEntity
import com.eskisehir.events.data.repository.MapsRepository
import com.eskisehir.events.data.repository.RoadmapRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

data class RoadmapSegmentRoute(
    val fromIndex: Int,
    val toIndex: Int,
    val durationSeconds: Long = 0,
    val distanceMeters: Long = 0,
    val encodedPolyline: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
)

data class RoadmapUiState(
    val stops: List<RoadmapStopEntity> = emptyList(),
    val segmentRoutes: Map<String, RoadmapSegmentRoute> = emptyMap(),
    val totalDurationSeconds: Long = 0,
    val totalDistanceMeters: Long = 0,
    val selectedTravelMode: String = "DRIVE",
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

    private var calculationJob: Job? = null

    init {
        observeStops()
    }

    private fun observeStops() {
        viewModelScope.launch {
            roadmapRepository.getAllStops().collectLatest { stops ->
                Log.d("ROADMAP", "Loaded stops count=${stops.size}")
                _uiState.value = _uiState.value.copy(stops = stops)
                calculateAllSegmentRoutes(stops, _uiState.value.selectedTravelMode)
            }
        }
    }

    fun setTravelMode(mode: String) {
        if (_uiState.value.selectedTravelMode != mode) {
            _uiState.value = _uiState.value.copy(selectedTravelMode = mode)
            calculateAllSegmentRoutes(_uiState.value.stops, mode)
        }
    }

    private fun calculateAllSegmentRoutes(stops: List<RoadmapStopEntity>, travelMode: String) {
        calculationJob?.cancel()
        if (stops.size < 2) {
            _uiState.value = _uiState.value.copy(
                segmentRoutes = emptyMap(),
                totalDurationSeconds = 0,
                totalDistanceMeters = 0
            )
            return
        }

        calculationJob = viewModelScope.launch {
            val segmentRoutes = mutableMapOf<String, RoadmapSegmentRoute>()
            var totalDuration = 0L
            var totalDistance = 0L

            for (i in 0 until stops.size - 1) {
                val fromStop = stops[i]
                val toStop = stops[i + 1]
                val key = "${i}_${i + 1}"

                Log.d("ROADMAP_ROUTE", "Calculating segment from=${fromStop.title} to=${toStop.title} mode=$travelMode")
                
                segmentRoutes[key] = RoadmapSegmentRoute(fromIndex = i, toIndex = i + 1, isLoading = true)
                _uiState.value = _uiState.value.copy(segmentRoutes = segmentRoutes.toMap())

                val result = mapsRepository.computeRoute(
                    originLat = fromStop.latitude,
                    originLng = fromStop.longitude,
                    destinationLat = toStop.latitude,
                    destinationLng = toStop.longitude,
                    travelMode = travelMode
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
                        error = error.message ?: "Rota hatası"
                    )
                }

                _uiState.value = _uiState.value.copy(
                    segmentRoutes = segmentRoutes.toMap(),
                    totalDurationSeconds = totalDuration,
                    totalDistanceMeters = totalDistance
                )
            }
        }
    }

    fun addStop(eventId: Long, title: String, latitude: Double, longitude: Double, locationName: String, address: String, date: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(error = null)
            val result = roadmapRepository.addStop(eventId, title, latitude, longitude, locationName, address, date)
            result.onFailure { error ->
                _uiState.value = _uiState.value.copy(error = error.message)
            }
        }
    }

    fun removeStop(eventId: Long) {
        viewModelScope.launch {
            try {
                roadmapRepository.removeStop(eventId)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = "Durak silinemedi: ${e.message}")
            }
        }
    }

    fun moveStopUp(index: Int) {
        if (index > 0) {
            val stops = _uiState.value.stops.toMutableList()
            val stop = stops.removeAt(index)
            stops.add(index - 1, stop)
            reorderStops(stops)
        }
    }

    fun moveStopDown(index: Int) {
        if (index < _uiState.value.stops.size - 1) {
            val stops = _uiState.value.stops.toMutableList()
            val stop = stops.removeAt(index)
            stops.add(index + 1, stop)
            reorderStops(stops)
        }
    }

    private fun reorderStops(newOrder: List<RoadmapStopEntity>) {
        viewModelScope.launch {
            try {
                roadmapRepository.reorderStops(newOrder)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = "Sıralama değiştirilemedi")
            }
        }
    }

    fun clearRoadmap() {
        viewModelScope.launch {
            try {
                roadmapRepository.clearRoadmap()
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = "Rota temizlenemedi")
            }
        }
    }

    fun isEventInRoadmap(eventId: Long, callback: (Boolean) -> Unit) {
        viewModelScope.launch {
            callback(roadmapRepository.isEventInRoadmap(eventId))
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}
