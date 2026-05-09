package com.eskisehir.eventapp.ui.weather

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eskisehir.events.data.repository.WeatherRepository
import com.eskisehir.events.domain.model.HourlyWeather
import com.eskisehir.events.domain.model.WeatherInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class WeatherUiState {
    object Loading  : WeatherUiState()
    data class Success(val data: WeatherInfo) : WeatherUiState()
    data class Error(val message: String)     : WeatherUiState()
}

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<WeatherUiState>(WeatherUiState.Loading)
    val uiState: StateFlow<WeatherUiState> = _uiState.asStateFlow()

    init {
        loadWeather()
    }

    fun loadWeather() {
        viewModelScope.launch {
            _uiState.value = WeatherUiState.Loading
            weatherRepository.getWeather().fold(
                onSuccess = { _uiState.value = WeatherUiState.Success(it) },
                onFailure = { _uiState.value = WeatherUiState.Error("Hava durumu bilgisi alınamadı.") }
            )
        }
    }

    /** Returns the best matching hourly slot for the given event date string. */
    fun getHourlyForEvent(eventDateStr: String): HourlyWeather? {
        val state = _uiState.value
        if (state !is WeatherUiState.Success) return null
        return weatherRepository.findHourlyForEvent(state.data.hourly, eventDateStr)
    }
}
