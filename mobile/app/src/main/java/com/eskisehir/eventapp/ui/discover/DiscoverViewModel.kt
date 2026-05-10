package com.eskisehir.eventapp.ui.discover

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eskisehir.eventapp.data.model.RecommendationPreferences
import com.eskisehir.eventapp.data.model.SampleData
import com.eskisehir.eventapp.data.repository.AiRecommendationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DiscoverViewModel @Inject constructor(
    private val aiRepository: AiRecommendationRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<RecommendationUiState>(RecommendationUiState.Idle)
    val uiState: StateFlow<RecommendationUiState> = _uiState.asStateFlow()

    private val _preferences = MutableStateFlow(RecommendationPreferences())
    val preferences: StateFlow<RecommendationPreferences> = _preferences.asStateFlow()

    fun updatePreferences(newPrefs: RecommendationPreferences) {
        _preferences.value = newPrefs
    }

    fun getRecommendations() {
        val currentPrefs = _preferences.value
        if (currentPrefs.selectedCategories.isEmpty()) {
            _uiState.value = RecommendationUiState.Error("Lütfen en az bir kategori seçin.")
            return
        }

        viewModelScope.launch {
            _uiState.value = RecommendationUiState.Loading
            aiRepository.getRecommendations(currentPrefs).fold(
                onSuccess = { results ->
                    val eventsWithRecommendations = results.mapNotNull { rec ->
                        val event = SampleData.events.find { it.id == rec.eventId }
                        if (event != null) event to rec else null
                    }
                    if (eventsWithRecommendations.isEmpty()) {
                        _uiState.value = RecommendationUiState.Error("Şu anda tercihlerine uygun etkinlik bulamadık. Farklı tercihler deneyebilirsin.")
                    } else {
                        _uiState.value = RecommendationUiState.Success(eventsWithRecommendations)
                    }
                },
                onFailure = { error ->
                    _uiState.value = RecommendationUiState.Error(error.message ?: "Beklenmeyen bir hata oluştu.")
                }
            )
        }
    }

    fun reset() {
        _uiState.value = RecommendationUiState.Idle
    }
}
