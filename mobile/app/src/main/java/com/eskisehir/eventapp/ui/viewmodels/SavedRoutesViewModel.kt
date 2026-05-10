package com.eskisehir.eventapp.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eskisehir.events.data.local.entity.SavedRouteEntity
import com.eskisehir.events.data.repository.SavedRouteRepository
import com.eskisehir.events.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SavedRoutesViewModel @Inject constructor(
    private val savedRouteRepository: SavedRouteRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _savedRoutes = MutableStateFlow<List<SavedRouteEntity>>(emptyList())
    val savedRoutes: StateFlow<List<SavedRouteEntity>> = _savedRoutes.asStateFlow()

    init {
        loadSavedRoutes()
    }

    private fun loadSavedRoutes() {
        viewModelScope.launch {
            userRepository.getUserProfile().collectLatest { user ->
                if (user != null) {
                    savedRouteRepository.getSavedRoutesByUser(user.email).collect { routes ->
                        _savedRoutes.value = routes
                    }
                } else {
                    _savedRoutes.value = emptyList()
                }
            }
        }
    }

    fun deleteRoute(id: Long) {
        viewModelScope.launch {
            savedRouteRepository.deleteRoute(id)
        }
    }
}
