package com.eskisehir.events.data.repository

import com.eskisehir.eventapp.BuildConfig
import com.eskisehir.events.data.remote.maps.RoutesApiService
import com.eskisehir.events.data.remote.maps.dto.LatLngDto
import com.eskisehir.events.data.remote.maps.dto.LocationDto
import com.eskisehir.events.data.remote.maps.dto.RoutesRequestDto
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MapsRepository @Inject constructor(
    private val routesApiService: RoutesApiService
) {
    /**
     * Compute route between two locations for a specific travel mode
     * @return Triple of (durationSeconds, distanceMeters, encodedPolyline)
     */
    suspend fun computeRoute(
        originLat: Double,
        originLng: Double,
        destinationLat: Double,
        destinationLng: Double,
        travelMode: String = "DRIVE"
    ): Result<RouteData> {
        return try {
            val request = RoutesRequestDto(
                origin = LocationDto(LatLngDto(originLat, originLng)),
                destination = LocationDto(LatLngDto(destinationLat, destinationLng)),
                travelMode = travelMode
            )

            val response = routesApiService.computeRoutes(
                apiKey = BuildConfig.GOOGLE_MAPS_API_KEY,
                request = request
            )

            if (response.error != null) {
                return Result.failure(Exception("Routes API Error: ${response.error.message}"))
            }

            val route = response.routes.firstOrNull()
                ?: return Result.failure(Exception("No route found"))

            val durationSeconds = route.duration?.removeSuffix("s")?.toLongOrNull() ?: 0L
            val distanceMeters = route.distanceMeters
            val encodedPolyline = route.polyline?.encodedPolyline ?: ""

            Result.success(RouteData(durationSeconds, distanceMeters, encodedPolyline))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    data class RouteData(
        val durationSeconds: Long,
        val distanceMeters: Long,
        val encodedPolyline: String
    )
}
