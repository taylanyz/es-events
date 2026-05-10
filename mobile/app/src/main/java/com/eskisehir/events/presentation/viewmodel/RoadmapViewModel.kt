package com.eskisehir.events.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eskisehir.eventapp.data.model.RoutePlaceSuggestion
import com.eskisehir.eventapp.data.model.SavedRouteSegment
import com.eskisehir.eventapp.data.model.SavedRouteStop
import com.eskisehir.events.data.local.entity.RoadmapStopEntity
import com.eskisehir.events.data.local.entity.SavedRouteEntity
import com.eskisehir.events.data.repository.MapsRepository
import com.eskisehir.events.data.repository.PlacesRepository
import com.eskisehir.events.data.repository.RoadmapRepository
import com.eskisehir.events.data.repository.SavedRouteRepository
import com.eskisehir.events.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

data class RoadmapSegmentRoute(
    val fromIndex: Int,
    val toIndex: Int,
    val durationSeconds: Long = 0,
    val distanceMeters: Long = 0,
    val encodedPolyline: String = "",
    val suggestions: List<RoutePlaceSuggestion> = emptyList(),
    val isLoading: Boolean = false,
    val isSuggestionsLoading: Boolean = false,
    val error: String? = null
)

data class RoadmapUiState(
    val stops: List<RoadmapStopEntity> = emptyList(),
    val segmentRoutes: Map<String, RoadmapSegmentRoute> = emptyMap(),
    val totalDurationSeconds: Long = 0,
    val totalDistanceMeters: Long = 0,
    val selectedTravelMode: String = "DRIVE",
    val isLoading: Boolean = false,
    val error: String? = null,
    val saveSuccess: Boolean = false
)

@HiltViewModel
class RoadmapViewModel @Inject constructor(
    private val roadmapRepository: RoadmapRepository,
    private val mapsRepository: MapsRepository,
    private val placesRepository: PlacesRepository,
    private val savedRouteRepository: SavedRouteRepository,
    private val userRepository: UserRepository
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

    fun fetchSuggestionsForSegment(key: String) {
        val segment = _uiState.value.segmentRoutes[key] ?: return
        if (segment.encodedPolyline.isEmpty()) return
        
        val fromStop = _uiState.value.stops[segment.fromIndex]
        val toStop = _uiState.value.stops[segment.toIndex]

        viewModelScope.launch {
            val updatedSegments = _uiState.value.segmentRoutes.toMutableMap()
            updatedSegments[key] = segment.copy(isSuggestionsLoading = true)
            _uiState.value = _uiState.value.copy(segmentRoutes = updatedSegments.toMap())

            val result = placesRepository.searchPlacesAlongRoute(
                polyline = segment.encodedPolyline,
                segmentIndex = segment.fromIndex,
                fromEventId = fromStop.eventId,
                toEventId = toStop.eventId
            )

            result.onSuccess { suggestions ->
                updatedSegments[key] = updatedSegments[key]!!.copy(
                    suggestions = suggestions,
                    isSuggestionsLoading = false
                )
            }.onFailure {
                updatedSegments[key] = updatedSegments[key]!!.copy(
                    isSuggestionsLoading = false
                )
            }
            _uiState.value = _uiState.value.copy(segmentRoutes = updatedSegments.toMap())
        }
    }

    fun addSuggestionToRoute(suggestion: RoutePlaceSuggestion, insertAtIndex: Int) {
        viewModelScope.launch {
            // Create a pseudo-eventId from suggestion id
            val pseudoEventId = suggestion.id.hashCode().toLong().let { if (it > 0) -it else it }

            val newStop = RoadmapStopEntity(
                eventId = pseudoEventId,
                title = suggestion.name,
                latitude = suggestion.latitude,
                longitude = suggestion.longitude,
                locationName = suggestion.primaryType ?: "Mola Noktası",
                address = suggestion.address,
                date = _uiState.value.stops.firstOrNull()?.date ?: "2026-05-10T12:00",
                stopOrder = insertAtIndex,
                isAiRecommended = true // Mark as AI suggestion
            )

            val result = roadmapRepository.addSuggestedStop(newStop, insertAtIndex)
            result.onFailure { error ->
                _uiState.value = _uiState.value.copy(error = error.message)
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

    fun saveCurrentRoute(title: String) {
        viewModelScope.launch {
            val user = userRepository.getUserProfile().first()
            if (user == null) {
                _uiState.value = _uiState.value.copy(error = "Rotaları kaydetmek için giriş yapmalısınız.")
                return@launch
            }

            val currentState = _uiState.value
            if (currentState.stops.size < 2) {
                _uiState.value = _uiState.value.copy(error = "Rotayı kaydetmek için en az 2 durak eklemelisiniz.")
                return@launch
            }

            val savedRoute = SavedRouteEntity(
                userEmail = user.email,
                title = title.ifBlank { "Rota Planım - ${currentState.stops.first().date.split("T").first()}" },
                date = currentState.stops.first().date,
                totalDurationSeconds = currentState.totalDurationSeconds,
                totalDistanceMeters = currentState.totalDistanceMeters,
                travelMode = currentState.selectedTravelMode,
                stops = currentState.stops.map { 
                    SavedRouteStop(
                        eventId = it.eventId,
                        title = it.title,
                        latitude = it.latitude,
                        longitude = it.longitude,
                        locationName = it.locationName,
                        address = it.address,
                        stopOrder = it.stopOrder,
                        isAiRecommended = it.isAiRecommended
                    )
                },
                segments = currentState.segmentRoutes.values.map { 
                    SavedRouteSegment(
                        fromIndex = it.fromIndex,
                        toIndex = it.toIndex,
                        durationSeconds = it.durationSeconds,
                        distanceMeters = it.distanceMeters,
                        encodedPolyline = it.encodedPolyline
                    )
                }
            )

            try {
                savedRouteRepository.saveRoute(savedRoute)
                _uiState.value = _uiState.value.copy(saveSuccess = true)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = "Rota kaydedilemedi: ${e.message}")
            }
        }
    }

    fun loadSavedRoute(savedRoute: SavedRouteEntity) {
        viewModelScope.launch {
            try {
                roadmapRepository.clearRoadmap()
                val roadmapStops = savedRoute.stops.map { 
                    RoadmapStopEntity(
                        eventId = it.eventId,
                        title = it.title,
                        latitude = it.latitude,
                        longitude = it.longitude,
                        locationName = it.locationName,
                        address = it.address,
                        date = savedRoute.date,
                        stopOrder = it.stopOrder,
                        isAiRecommended = it.isAiRecommended
                    )
                }
                roadmapRepository.reorderStops(roadmapStops)
                _uiState.value = _uiState.value.copy(selectedTravelMode = savedRoute.travelMode)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = "Rota yüklenemedi")
            }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null, saveSuccess = false)
    }
}
