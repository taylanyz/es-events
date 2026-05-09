package com.eskisehir.events.presentation.viewmodel

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eskisehir.events.data.repository.MapsRepository
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

sealed class RouteUiState {
    object Idle : RouteUiState()
    object Loading : RouteUiState()
    data class Success(
        val durationSeconds: Long,
        val distanceMeters: Long,
        val encodedPolyline: String
    ) : RouteUiState()
    data class Error(val message: String) : RouteUiState()
}

data class MapsUiState(
    val userLocation: LatLng? = null,
    val driveRoute: RouteUiState = RouteUiState.Idle,
    val walkRoute: RouteUiState = RouteUiState.Idle,
    val transitRoute: RouteUiState = RouteUiState.Idle,
    val locationPermissionGranted: Boolean = false,
    val isLoadingLocation: Boolean = false,
    val locationError: String? = null
)

@HiltViewModel
class MapsViewModel @Inject constructor(
    private val mapsRepository: MapsRepository,
    private val fusedLocationProviderClient: FusedLocationProviderClient,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _uiState = MutableStateFlow(MapsUiState())
    val uiState: StateFlow<MapsUiState> = _uiState.asStateFlow()

    init {
        checkLocationPermission()
    }

    /**
     * Check if location permission is granted
     */
    fun checkLocationPermission() {
        val permissionGranted = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        _uiState.value = _uiState.value.copy(locationPermissionGranted = permissionGranted)
    }

    /**
     * Request to get user's current location
     */
    fun getUserLocation() {
        if (!_uiState.value.locationPermissionGranted) {
            _uiState.value = _uiState.value.copy(
                locationError = "Location permission not granted"
            )
            return
        }

        _uiState.value = _uiState.value.copy(isLoadingLocation = true, locationError = null)

        viewModelScope.launch {
            try {
                val location = fusedLocationProviderClient.lastLocation.await()
                if (location != null) {
                    val userLatLng = LatLng(location.latitude, location.longitude)
                    _uiState.value = _uiState.value.copy(
                        userLocation = userLatLng,
                        isLoadingLocation = false
                    )
                } else {
                    _uiState.value = _uiState.value.copy(
                        isLoadingLocation = false,
                        locationError = "Could not determine current location"
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoadingLocation = false,
                    locationError = "Error getting location: ${e.message}"
                )
            }
        }
    }

    /**
     * Calculate route between two points
     * @param originLat Latitude of origin
     * @param originLng Longitude of origin
     * @param destLat Latitude of destination
     * @param destLng Longitude of destination
     * @param travelMode DRIVE, WALK, or TRANSIT
     */
    fun calculateRoute(
        originLat: Double,
        originLng: Double,
        destLat: Double,
        destLng: Double,
        travelMode: String = "DRIVE"
    ) {
        viewModelScope.launch {
            // Update loading state based on travel mode
            when (travelMode) {
                "DRIVE" -> _uiState.value = _uiState.value.copy(driveRoute = RouteUiState.Loading)
                "WALK" -> _uiState.value = _uiState.value.copy(walkRoute = RouteUiState.Loading)
                "TRANSIT" -> _uiState.value = _uiState.value.copy(transitRoute = RouteUiState.Loading)
            }

            val result = mapsRepository.computeRoute(
                originLat = originLat,
                originLng = originLng,
                destinationLat = destLat,
                destinationLng = destLng,
                travelMode = travelMode
            )

            result.onSuccess { routeData ->
                val successState = RouteUiState.Success(
                    durationSeconds = routeData.durationSeconds,
                    distanceMeters = routeData.distanceMeters,
                    encodedPolyline = routeData.encodedPolyline
                )
                when (travelMode) {
                    "DRIVE" -> _uiState.value = _uiState.value.copy(driveRoute = successState)
                    "WALK" -> _uiState.value = _uiState.value.copy(walkRoute = successState)
                    "TRANSIT" -> _uiState.value = _uiState.value.copy(transitRoute = successState)
                }
            }.onFailure { error ->
                val errorState = RouteUiState.Error(
                    message = error.message ?: "Unknown error calculating route"
                )
                when (travelMode) {
                    "DRIVE" -> _uiState.value = _uiState.value.copy(driveRoute = errorState)
                    "WALK" -> _uiState.value = _uiState.value.copy(walkRoute = errorState)
                    "TRANSIT" -> _uiState.value = _uiState.value.copy(transitRoute = errorState)
                }
            }
        }
    }

    /**
     * Calculate routes for all travel modes from a point
     */
    fun calculateAllRoutes(
        originLat: Double,
        originLng: Double,
        destLat: Double,
        destLng: Double
    ) {
        calculateRoute(originLat, originLng, destLat, destLng, "DRIVE")
        calculateRoute(originLat, originLng, destLat, destLng, "WALK")
        calculateRoute(originLat, originLng, destLat, destLng, "TRANSIT")
    }

    /**
     * Notify ViewModel that location permission status has changed
     */
    fun onPermissionStatusChanged(granted: Boolean) {
        _uiState.value = _uiState.value.copy(locationPermissionGranted = granted)
        if (granted) {
            getUserLocation()
        }
    }

    /**
     * Clear location error
     */
    fun clearLocationError() {
        _uiState.value = _uiState.value.copy(locationError = null)
    }
}
