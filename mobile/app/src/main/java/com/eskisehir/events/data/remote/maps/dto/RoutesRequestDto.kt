package com.eskisehir.events.data.remote.maps.dto

import com.google.gson.annotations.SerializedName

data class RoutesRequestDto(
    @SerializedName("origin")
    val origin: LocationDto,
    @SerializedName("destination")
    val destination: LocationDto,
    @SerializedName("travelMode")
    val travelMode: String, // DRIVE, WALK, TRANSIT
    @SerializedName("routingPreference")
    val routingPreference: String = "TRAFFIC_AWARE"
)

data class LocationDto(
    @SerializedName("latLng")
    val latLng: LatLngDto
)

data class LatLngDto(
    @SerializedName("latitude")
    val latitude: Double,
    @SerializedName("longitude")
    val longitude: Double
)
