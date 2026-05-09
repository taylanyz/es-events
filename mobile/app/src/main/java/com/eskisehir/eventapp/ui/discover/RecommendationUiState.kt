package com.eskisehir.eventapp.ui.discover

import com.eskisehir.eventapp.data.model.Event
import com.eskisehir.eventapp.data.model.EventRecommendation

sealed class RecommendationUiState {
    object Idle : RecommendationUiState()
    object Loading : RecommendationUiState()
    data class Success(val recommendations: List<Pair<Event, EventRecommendation>>) : RecommendationUiState()
    data class Error(val message: String) : RecommendationUiState()
}
