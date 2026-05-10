package com.eskisehir.eventapp.data.remote.places.dto

import com.google.gson.annotations.SerializedName

data class PlacesSearchRequestDto(
    @SerializedName("textQuery")
    val textQuery: String,
    @SerializedName("searchAlongRouteParameters")
    val searchAlongRouteParameters: SearchAlongRouteParametersDto,
    @SerializedName("languageCode")
    val languageCode: String = "tr",
    @SerializedName("regionCode")
    val regionCode: String = "TR",
    @SerializedName("maxResultCount")
    val maxResultCount: Int = 10
)

data class SearchAlongRouteParametersDto(
    @SerializedName("polyline")
    val polyline: PolylineDto
)

data class PolylineDto(
    @SerializedName("encodedPolyline")
    val encodedPolyline: String
)
