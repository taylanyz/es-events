package com.eskisehir.eventapp.data.remote.ai

import com.eskisehir.eventapp.data.remote.ai.dto.GeminiRequestDto
import com.eskisehir.eventapp.data.remote.ai.dto.GeminiResponseDto
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

/**
 * Retrofit service for Gemini API.
 * Updated to use gemini-2.0-flash.
 */
interface GeminiApiService {
    @POST("v1beta/models/gemini-2.0-flash:generateContent")
    suspend fun generateContent(
        @Query("key") apiKey: String,
        @Body request: GeminiRequestDto
    ): GeminiResponseDto
}
