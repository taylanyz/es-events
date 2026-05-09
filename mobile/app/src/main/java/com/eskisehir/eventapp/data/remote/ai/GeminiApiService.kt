package com.eskisehir.eventapp.data.remote.ai

import com.eskisehir.eventapp.data.remote.ai.dto.GeminiRequestDto
import com.eskisehir.eventapp.data.remote.ai.dto.GeminiResponseDto
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

interface GeminiApiService {
    @POST("v1beta/models/gemini-1.5-flash:generateContent")
    suspend fun generateContent(
        @Query("key") apiKey: String,
        @Body request: GeminiRequestDto
    ): GeminiResponseDto
}
