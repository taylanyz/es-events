package com.eskisehir.events.data.remote.maps.dto

import com.google.gson.annotations.SerializedName

data class RoutesResponseDto(
    @SerializedName("routes")
    val routes: List<RouteDto> = emptyList(),
    @SerializedName("error")
    val error: ErrorDto? = null
)

data class RouteDto(
    @SerializedName("duration")
    val duration: String? = null, // "3600s"
    @SerializedName("distanceMeters")
    val distanceMeters: Long = 0,
    @SerializedName("polyline")
    val polyline: PolylineDto? = null
)

data class PolylineDto(
    @SerializedName("encodedPolyline")
    val encodedPolyline: String = ""
)

data class ErrorDto(
    @SerializedName("code")
    val code: Int = 0,
    @SerializedName("message")
    val message: String = "",
    @SerializedName("status")
    val status: String = ""
)
