package com.eskisehir.eventapp.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eskisehir.events.data.local.entity.SavedRouteEntity
import com.eskisehir.events.data.repository.SavedRouteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SavedRouteDetailViewModel @Inject constructor(
    private val savedRouteRepository: SavedRouteRepository
) : ViewModel() {

    private val _route = MutableStateFlow<SavedRouteEntity?>(null)
    val route: StateFlow<SavedRouteEntity?> = _route.asStateFlow()

    fun loadRoute(id: Long) {
        viewModelScope.launch {
            _route.value = savedRouteRepository.getSavedRouteById(id)
        }
    }

    fun deleteRoute(id: Long) {
        viewModelScope.launch {
            savedRouteRepository.deleteRoute(id)
        }
    }
}
