package com.eskisehir.events.data.repository

import android.util.Log
import com.eskisehir.eventapp.BuildConfig
import com.eskisehir.events.data.remote.maps.RoutesApiService
import com.eskisehir.events.data.remote.maps.dto.LatLngDto
import com.eskisehir.events.data.remote.maps.dto.LocationWrapperDto
import com.eskisehir.events.data.remote.maps.dto.RoutesRequestDto
import com.eskisehir.events.data.remote.maps.dto.WaypointDto
import retrofit2.HttpException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MapsRepository @Inject constructor(
    private val routesApiService: RoutesApiService
) {
    /**
     * Compute route between two locations for a specific travel mode
     */
    suspend fun computeRoute(
        originLat: Double,
        originLng: Double,
        destinationLat: Double,
        destinationLng: Double,
        travelMode: String = "DRIVE"
    ): Result<RouteData> {
        val apiKey = BuildConfig.GOOGLE_MAPS_API_KEY
        
        if (apiKey.isEmpty() || apiKey == "BURAYA_GOOGLE_API_KEY_YAZILACAK") {
            return Result.failure(Exception("API Key missing"))
        }

        // Log the coordinates for debugging
        Log.d("RoutesDebug", "Origin: $originLat, $originLng")
        Log.d("RoutesDebug", "Destination: $destinationLat, $destinationLng")
        Log.d("RoutesDebug", "Travel mode: $travelMode")

        return try {
            val request = RoutesRequestDto(
                origin = WaypointDto(
                    location = LocationWrapperDto(
                        latLng = LatLngDto(originLat, originLng)
                    )
                ),
                destination = WaypointDto(
                    location = LocationWrapperDto(
                        latLng = LatLngDto(destinationLat, destinationLng)
                    )
                ),
                travelMode = travelMode,
                routingPreference = if (travelMode == "DRIVE") "TRAFFIC_AWARE" else null,
                languageCode = "tr-TR",
                units = "METRIC"
            )

            val response = routesApiService.computeRoutes(
                apiKey = apiKey,
                fieldMask = "routes.duration,routes.distanceMeters,routes.polyline.encodedPolyline",
                request = request
            )

            if (response.error != null) {
                Log.e("RoutesAPI", "Response Error Code: ${response.error.code} Body: ${response.error.message}")
                return Result.failure(Exception(response.error.message))
            }

            val route = response.routes.firstOrNull()
            if (route == null) {
                Log.e("RoutesAPI", "Routes API returned empty routes list for mode $travelMode.")
                return Result.failure(Exception("Rota bulunamadı."))
            }

            val durationSeconds = route.duration?.removeSuffix("s")?.toLongOrNull() ?: 0L
            val distanceMeters = route.distanceMeters
            val encodedPolyline = route.polyline?.encodedPolyline ?: ""

            Log.d("RoutesAPI", "Success: distance=$distanceMeters meters, duration=$durationSeconds seconds")

            Result.success(RouteData(durationSeconds, distanceMeters, encodedPolyline))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            Log.e("RoutesAPI", "HTTP ERROR Code: ${e.code()} Body: $errorBody")
            Result.failure(Exception("Google servis hatası"))
        } catch (e: Exception) {
            Log.e("RoutesAPI", "General Exception: ${e.message}")
            Result.failure(e)
        }
    }

    data class RouteData(
        val durationSeconds: Long,
        val distanceMeters: Long,
        val encodedPolyline: String
    )
}
