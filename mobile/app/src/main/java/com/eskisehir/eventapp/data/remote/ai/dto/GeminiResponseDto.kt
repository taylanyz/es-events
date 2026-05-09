package com.eskisehir.eventapp.data.remote.ai.dto

import com.google.gson.annotations.SerializedName

data class GeminiResponseDto(
    @SerializedName("candidates")
    val candidates: List<GeminiCandidate>? = null
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

// Internal parser model for the JSON block Gemini returns inside text
data class AIRecommendationList(
    @SerializedName("recommendations")
    val recommendations: List<AIEventRecommendation>
)

data class AIEventRecommendation(
    @SerializedName("eventId")
    val eventId: Long,
    @SerializedName("score")
    val score: Int,
    @SerializedName("reason")
    val reason: String,
    @SerializedName("matchedPreferences")
    val matchedPreferences: List<String>
)
