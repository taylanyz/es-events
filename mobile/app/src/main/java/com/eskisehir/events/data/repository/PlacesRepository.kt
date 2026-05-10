package com.eskisehir.events.data.repository

import android.util.Log
import com.eskisehir.eventapp.BuildConfig
import com.eskisehir.eventapp.data.model.RoutePlaceSuggestion
import com.eskisehir.eventapp.data.remote.places.PlacesApiService
import com.eskisehir.eventapp.data.remote.places.dto.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PlacesRepository @Inject constructor(
    private val placesApiService: PlacesApiService
) {
    private val TAG = "PlacesAlongRoute"

    suspend fun searchPlacesAlongRoute(
        polyline: String,
        segmentIndex: Int,
        fromEventId: Long,
        toEventId: Long
    ): Result<List<RoutePlaceSuggestion>> {
        val apiKey = BuildConfig.GOOGLE_MAPS_API_KEY // Use shared key

        if (apiKey.isEmpty() || apiKey == "BURAYA_GOOGLE_API_KEY_YAZILACAK") {
            Log.e(TAG, "PLACES_API_KEY is empty")
            return Result.failure(Exception("API Key missing"))
        }

        Log.d(TAG, "Places request started for segment $segmentIndex")
        
        return try {
            val request = PlacesSearchRequestDto(
                textQuery = "restaurant cafe bakery coffee dessert",
                searchAlongRouteParameters = SearchAlongRouteParametersDto(
                    polyline = PolylineDto(encodedPolyline = polyline)
                ),
                maxResultCount = 10
            )

            val fieldMask = "places.id,places.displayName,places.formattedAddress,places.location,places.rating,places.userRatingCount,places.primaryType,places.types"

            val response = placesApiService.searchText(
                apiKey = apiKey,
                fieldMask = fieldMask,
                request = request
            )

            val rawPlaces = response.places ?: emptyList()
            Log.d(TAG, "Gelen place sayısı: ${rawPlaces.size}")

            val filteredSuggestions = rawPlaces
                .filter { it.rating != null && it.rating >= 4.0 }
                .filter { (it.userRatingCount ?: 0) >= 30 }
                .filter { isValidEskisehirCoordinate(it.location.latitude, it.location.longitude) }
                .map { place ->
                    RoutePlaceSuggestion(
                        id = place.id,
                        name = place.displayName.text,
                        address = place.formattedAddress,
                        latitude = place.location.latitude,
                        longitude = place.location.longitude,
                        rating = place.rating,
                        userRatingCount = place.userRatingCount,
                        primaryType = place.primaryType,
                        types = place.types ?: emptyList(),
                        reason = generateReason(place),
                        segmentIndex = segmentIndex,
                        fromEventId = fromEventId,
                        toEventId = toEventId
                    )
                }
                .take(5)

            Log.d(TAG, "Filtre sonrası place sayısı: ${filteredSuggestions.size}")
            Result.success(filteredSuggestions)

        } catch (e: Exception) {
            Log.e(TAG, "Error in searchPlacesAlongRoute: ${e.message}", e)
            Result.failure(e)
        }
    }

    private fun isValidEskisehirCoordinate(lat: Double, lng: Double): Boolean {
        return lat in 39.0..40.5 && lng in 29.5..31.5
    }

    private fun generateReason(place: PlaceDto): String {
        val type = place.primaryType?.replace("_", " ")?.lowercase() ?: "mekan"
        val rating = place.rating ?: 0.0
        val reviewCount = place.userRatingCount ?: 0
        
        return when {
            rating >= 4.5 && reviewCount >= 100 -> {
                "Bu $type iki etkinlik arasındaki rota üzerinde kısa bir kahve molası için mükemmel görünüyor. Puanı çok yüksek ve yorum sayısı fazla olduğu için uğramayı unutma."
            }
            type.contains("restaurant") || type.contains("food") -> {
                "Rota üzerinde kalması ve yüksek kullanıcı puanı sayesinde etkinlikler arasında yemek molası vermek için iyi bir seçenek."
            }
            else -> {
                "Rota üzerinde yüksek puanlı bir mola noktası. Kısa bir dinlenme için bu $type ideal olabilir."
            }
        }
    }
}
