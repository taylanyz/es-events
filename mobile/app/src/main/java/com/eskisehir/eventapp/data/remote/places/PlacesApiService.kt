package com.eskisehir.eventapp.data.remote.places

import com.eskisehir.eventapp.data.remote.places.dto.PlacesSearchRequestDto
import com.eskisehir.eventapp.data.remote.places.dto.PlacesSearchResponseDto
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface PlacesApiService {
    @POST("v1/places:searchText")
    suspend fun searchText(
        @Header("X-Goog-Api-Key") apiKey: String,
        @Header("X-Goog-FieldMask") fieldMask: String,
        @Body request: PlacesSearchRequestDto
    ): PlacesSearchResponseDto
}
