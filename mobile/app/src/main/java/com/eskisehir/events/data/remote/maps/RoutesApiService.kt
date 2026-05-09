package com.eskisehir.events.data.remote.maps

import com.eskisehir.events.data.remote.maps.dto.RoutesRequestDto
import com.eskisehir.events.data.remote.maps.dto.RoutesResponseDto
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface RoutesApiService {
    @POST("directions/v2:computeRoutes")
    suspend fun computeRoutes(
        @Header("X-Goog-Api-Key") apiKey: String,
        @Header("X-Goog-FieldMask") fieldMask: String = "routes.duration,routes.distanceMeters,routes.polyline.encodedPolyline",
        @Body request: RoutesRequestDto
    ): RoutesResponseDto
}
