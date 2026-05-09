package com.eskisehir.events.data.remote.maps.dto

import com.google.gson.annotations.SerializedName

/**
 * Request body for Google Routes API computeRoutes endpoint.
 * Structure must be: Waypoint -> location -> latLng -> {latitude, longitude}
 */
data class RoutesRequestDto(
    @SerializedName("origin")
    val origin: WaypointDto,
    @SerializedName("destination")
    val destination: WaypointDto,
    @SerializedName("travelMode")
    val travelMode: String, // DRIVE, WALK, TRANSIT
    @SerializedName("routingPreference")
    val routingPreference: String? = null,
    @SerializedName("computeAlternativeRoutes")
    val computeAlternativeRoutes: Boolean = false,
    @SerializedName("languageCode")
    val languageCode: String = "tr-TR",
    @SerializedName("units")
    val units: String = "METRIC"
)

data class WaypointDto(
    @SerializedName("location")
    val location: LocationWrapperDto
)

data class LocationWrapperDto(
    @SerializedName("latLng")
    val latLng: LatLngDto
)

data class LatLngDto(
    @SerializedName("latitude")
    val latitude: Double,
    @SerializedName("longitude")
    val longitude: Double
)
