package com.eskisehir.eventapp.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eskisehir.eventapp.data.model.Event
import com.eskisehir.eventapp.data.model.SampleData
import com.eskisehir.events.domain.repository.EventRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val eventRepository: EventRepository
) : ViewModel() {

    private val _favoriteEvents = MutableStateFlow<List<Event>>(emptyList())
    val favoriteEvents: StateFlow<List<Event>> = _favoriteEvents.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun loadFavorites() {
        viewModelScope.launch {
            _isLoading.value = true
            val favoriteIds = eventRepository.getFavoriteIds()
            
            val events = favoriteIds.mapNotNull { id ->
                eventRepository.getEventById(id).fold(
                    onSuccess = { domainEvent ->
                        mapDomainToAppEvent(domainEvent)
                    },
                    onFailure = {
                        // Fallback to sample data if API fails
                        SampleData.events.find { it.id == id }
                    }
                )
            }
            _favoriteEvents.value = events
            _isLoading.value = false
        }
    }

    private fun mapDomainToAppEvent(domainEvent: com.eskisehir.events.domain.model.Event): Event {
        return Event(
            id = domainEvent.id,
            name = domainEvent.name,
            description = domainEvent.description,
            category = com.eskisehir.eventapp.data.model.Category.valueOf(domainEvent.category.name),
            latitude = domainEvent.latitude,
            longitude = domainEvent.longitude,
            venue = domainEvent.venue,
            date = domainEvent.date.toString(),
            price = domainEvent.price,
            imageUrl = domainEvent.imageUrl,
            tags = domainEvent.tags
        )
    }
}
