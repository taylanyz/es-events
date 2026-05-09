package com.eskisehir.events.presentation.viewmodel

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eskisehir.events.data.repository.MapsRepository
import com.google.android.gms.location.CurrentLocationRequest
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import kotlin.math.abs

sealed class RouteUiState {
    object Idle : RouteUiState()
    object Loading : RouteUiState()
    data class Success(
        val durationSeconds: Long,
        val distanceMeters: Long,
        val encodedPolyline: String,
        val isFallback: Boolean = false
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

    private val ESKISEHIR_CENTER = LatLng(39.7767, 30.5206)
    private val EMULATOR_DEFAULT = LatLng(37.422, -122.084)

    init {
        checkLocationPermission()
    }

    fun checkLocationPermission() {
        val permissionGranted = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        Log.d("LOCATION_TEST", "Permission check: $permissionGranted")
        _uiState.value = _uiState.value.copy(locationPermissionGranted = permissionGranted)
    }

    @SuppressLint("MissingPermission")
    fun getUserLocation() {
        if (!_uiState.value.locationPermissionGranted) {
            _uiState.value = _uiState.value.copy(
                locationError = "Konum izni verilmedi."
            )
            return
        }

        _uiState.value = _uiState.value.copy(isLoadingLocation = true, locationError = null)

        viewModelScope.launch {
            try {
                Log.d("LOCATION_TEST", "Fetching location...")
                
                var location = try {
                    fusedLocationProviderClient.lastLocation.await()
                } catch (e: Exception) {
                    null
                }
                
                if (location == null) {
                    val request = CurrentLocationRequest.Builder()
                        .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
                        .build()
                    location = try {
                        fusedLocationProviderClient.getCurrentLocation(request, null).await()
                    } catch (e: Exception) {
                        null
                    }
                }

                if (location != null) {
                    val userLatLng = LatLng(location.latitude, location.longitude)
                    Log.d("LOCATION_TEST", "Success: $userLatLng")
                    _uiState.value = _uiState.value.copy(
                        userLocation = userLatLng,
                        isLoadingLocation = false
                    )
                } else {
                    Log.e("LOCATION_TEST", "Location still null after attempts")
                    _uiState.value = _uiState.value.copy(
                        isLoadingLocation = false,
                        locationError = "Mevcut konum alınamadı. Eskişehir merkezden hesaplanacak."
                    )
                }
            } catch (e: Exception) {
                Log.e("LOCATION_TEST", "Fatal: ${e.message}")
                _uiState.value = _uiState.value.copy(
                    isLoadingLocation = false,
                    locationError = "Konum hatası: ${e.message}"
                )
            }
        }
    }

    fun calculateRoute(
        originLat: Double,
        originLng: Double,
        destLat: Double,
        destLng: Double,
        travelMode: String = "DRIVE"
    ) {
        var finalOriginLat = originLat
        var finalOriginLng = originLng
        var isFallback = false

        val isInvalid = originLat == 0.0 && originLng == 0.0
        val isEmulatorUSA = abs(originLat - EMULATOR_DEFAULT.latitude) < 0.1 && 
                            abs(originLng - EMULATOR_DEFAULT.longitude) < 0.1

        if (isInvalid || isEmulatorUSA) {
            Log.w("RoutesDebug", "Origin is invalid or default USA. Falling back to Eskişehir center.")
            finalOriginLat = ESKISEHIR_CENTER.latitude
            finalOriginLng = ESKISEHIR_CENTER.longitude
            isFallback = true
        }

        if (destLat == 0.0 || destLng == 0.0) {
            Log.e("ROUTES_API", "Abort: Destination coordinates are zero")
            return
        }

        viewModelScope.launch {
            when (travelMode) {
                "DRIVE" -> _uiState.value = _uiState.value.copy(driveRoute = RouteUiState.Loading)
                "WALK" -> _uiState.value = _uiState.value.copy(walkRoute = RouteUiState.Loading)
                "TRANSIT" -> _uiState.value = _uiState.value.copy(transitRoute = RouteUiState.Loading)
            }

            val result = mapsRepository.computeRoute(
                originLat = finalOriginLat,
                originLng = finalOriginLng,
                destinationLat = destLat,
                destinationLng = destLng,
                travelMode = travelMode
            )

            result.onSuccess { routeData ->
                val successState = RouteUiState.Success(
                    durationSeconds = routeData.durationSeconds,
                    distanceMeters = routeData.distanceMeters,
                    encodedPolyline = routeData.encodedPolyline,
                    isFallback = isFallback
                )
                when (travelMode) {
                    "DRIVE" -> _uiState.value = _uiState.value.copy(driveRoute = successState)
                    "WALK" -> _uiState.value = _uiState.value.copy(walkRoute = successState)
                    "TRANSIT" -> _uiState.value = _uiState.value.copy(transitRoute = successState)
                }
            }.onFailure { error ->
                val message = if (error.message == "Rota bulunamadı.") {
                    if (isEmulatorUSA) "Emulator konumu ABD'de. Lütfen Eskişehir yapın."
                    else "Bu ulaşım modu için rota bulunamadı."
                } else {
                    error.message ?: "Rota hatası"
                }
                
                val errorState = RouteUiState.Error(message = message)
                when (travelMode) {
                    "DRIVE" -> _uiState.value = _uiState.value.copy(driveRoute = errorState)
                    "WALK" -> _uiState.value = _uiState.value.copy(walkRoute = errorState)
                    "TRANSIT" -> _uiState.value = _uiState.value.copy(transitRoute = errorState)
                }
            }
        }
    }

    /**
     * Calculate all routes using individual coordinates (Legacy/Named parameter support)
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
     * Calculate all routes using LatLng objects
     */
    fun calculateAllRoutes(origin: LatLng?, destination: LatLng) {
        val lat = origin?.latitude ?: 0.0
        val lng = origin?.longitude ?: 0.0
        calculateAllRoutes(lat, lng, destination.latitude, destination.longitude)
    }

    fun onPermissionStatusChanged(granted: Boolean) {
        Log.d("LOCATION_TEST", "Permission status changed: $granted")
        _uiState.value = _uiState.value.copy(locationPermissionGranted = granted)
        if (granted) {
            getUserLocation()
        }
    }
}
