package com.eskisehir.events.data.remote.api

import com.eskisehir.events.data.remote.dto.EventDto
import com.eskisehir.events.data.remote.dto.RecommendationRequestDto
import com.eskisehir.events.data.remote.dto.RouteRequestDto
import com.eskisehir.events.data.remote.dto.InteractionRequestDto
import com.eskisehir.events.data.remote.dto.SmartRecommendationRequestDto
import com.eskisehir.events.data.remote.dto.RecommendationResponseDto
import retrofit2.http.*

/**
 * Retrofit interface defining all backend API endpoints.
 * Each method maps to a REST endpoint on the Spring Boot backend.
 */
interface EventApiService {

    /** GET /api/events — Fetch all events */
    @GET("api/events")
    suspend fun getEvents(): List<EventDto>

    /** GET /api/events/{id} — Fetch single event by ID */
    @GET("api/events/{id}")
    suspend fun getEventById(@Path("id") id: Long): EventDto

    /** GET /api/events/category/{category} — Filter events by category */
    @GET("api/events/category/{category}")
    suspend fun getEventsByCategory(@Path("category") category: String): List<EventDto>

    /** GET /api/events/search?q=... — Search events */
    @GET("api/events/search")
    suspend fun searchEvents(@Query("q") query: String): List<EventDto>

    /** POST /api/events/recommend — Get personalized recommendations */
    @POST("api/events/recommend")
    suspend fun getRecommendations(@Body request: RecommendationRequestDto): List<EventDto>

    /** POST /api/events/route — Get optimized route */
    @POST("api/events/route")
    suspend fun getRoute(@Body request: RouteRequestDto): List<EventDto>

    /** POST /api/interactions — Log user interaction (click) */
    @POST("api/interactions")
    suspend fun logInteraction(@Body request: InteractionRequestDto)

    /** POST /api/smart-recommendations — Get AI-powered smart recommendations */
    @POST("api/smart-recommendations")
    suspend fun getSmartRecommendations(@Body request: SmartRecommendationRequestDto): List<RecommendationResponseDto>
}
