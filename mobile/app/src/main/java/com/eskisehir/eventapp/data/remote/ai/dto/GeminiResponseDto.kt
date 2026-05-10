package com.eskisehir.eventapp.data.remote.ai.dto

import com.google.gson.annotations.SerializedName

/**
 * DTO for Gemini API response.
 * Includes error handling fields with nullable details to prevent parsing crashes.
 */
data class GeminiResponseDto(
    @SerializedName("candidates")
    val candidates: List<GeminiCandidate>? = null,
    @SerializedName("error")
    val error: GeminiError? = null
)

data class GeminiCandidate(
    @SerializedName("content")
    val content: GeminiContent? = null
)

data class GeminiContent(
    @SerializedName("parts")
    val parts: List<GeminiPart>? = null
)

data class GeminiPart(
    @SerializedName("text")
    val text: String? = null
)

data class GeminiError(
    @SerializedName("code")
    val code: Int? = null,
    @SerializedName("message")
    val message: String? = null,
    @SerializedName("status")
    val status: String? = null,
    @SerializedName("details")
    val details: List<Any>? = null 
)

/**
 * Structured output model for AI recommendations.
 * Uses default values to prevent parsing crashes.
 */
data class AiRecommendationResponse(
    @SerializedName("recommendations")
    val recommendations: List<AiRecommendationDto> = emptyList()
)

data class AiRecommendationDto(
    @SerializedName("eventId")
    val eventId: Long,
    @SerializedName("score")
    val score: Int = 0,
    @SerializedName("reason")
    val reason: String = "Bu etkinlik tercihlerinizle uyumlu görünüyor.",
    @SerializedName("matchedPreferences")
    val matchedPreferences: List<String> = emptyList()
)
