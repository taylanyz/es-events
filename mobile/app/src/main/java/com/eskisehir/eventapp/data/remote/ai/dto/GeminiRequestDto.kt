package com.eskisehir.eventapp.data.remote.ai.dto

import com.google.gson.annotations.SerializedName

data class GeminiRequestDto(
    @SerializedName("contents")
    val contents: List<Content>
)

data class Content(
    @SerializedName("parts")
    val parts: List<Part>
)

data class Part(
    @SerializedName("text")
    val text: String
)
