package com.eskisehir.eventapp.data.remote.places.dto

import com.google.gson.annotations.SerializedName

data class PlacesSearchResponseDto(
    @SerializedName("places")
    val places: List<PlaceDto>? = null
)

data class PlaceDto(
    @SerializedName("id")
    val id: String,
    @SerializedName("displayName")
    val displayName: DisplayNameDto,
    @SerializedName("formattedAddress")
    val formattedAddress: String,
    @SerializedName("location")
    val location: LocationDto,
    @SerializedName("rating")
    val rating: Double? = null,
    @SerializedName("userRatingCount")
    val userRatingCount: Int? = null,
    @SerializedName("primaryType")
    val primaryType: String? = null,
    @SerializedName("types")
    val types: List<String>? = null
)

data class DisplayNameDto(
    @SerializedName("text")
    val text: String,
    @SerializedName("languageCode")
    val languageCode: String
)

data class LocationDto(
    @SerializedName("latitude")
    val latitude: Double,
    @SerializedName("longitude")
    val longitude: Double
)
