package com.eskisehir.eventapp.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eskisehir.eventapp.data.model.Event
import com.eskisehir.eventapp.data.model.SampleData
import com.eskisehir.events.data.local.entity.EventStatus
import com.eskisehir.events.domain.repository.EventRepository
import com.eskisehir.events.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val eventRepository: EventRepository
) : ViewModel() {

    val userProfile = userRepository.getUserProfile()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val favoritePlaces = userRepository.getFavoritePlaces()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _attendedEvents = MutableStateFlow<List<Event>>(emptyList())
    val attendedEvents: StateFlow<List<Event>> = _attendedEvents.asStateFlow()

    private val _goingEvents = MutableStateFlow<List<Event>>(emptyList())
    val goingEvents: StateFlow<List<Event>> = _goingEvents.asStateFlow()

    private val _wantToGoEvents = MutableStateFlow<List<Event>>(emptyList())
    val wantToGoEvents: StateFlow<List<Event>> = _wantToGoEvents.asStateFlow()

    private val _favoriteEvents = MutableStateFlow<List<Event>>(emptyList())
    val favoriteEvents: StateFlow<List<Event>> = _favoriteEvents.asStateFlow()

    init {
        loadEventsByStatus()
        loadFavoriteEvents()
    }

    private fun loadEventsByStatus() {
        EventStatus.entries.forEach { status ->
            if (status == EventStatus.NONE) return@forEach
            
            val flow = when(status) {
                EventStatus.ATTENDED -> _attendedEvents
                EventStatus.GOING -> _goingEvents
                EventStatus.WANT_TO_GO -> _wantToGoEvents
                else -> null
            }

            flow?.let { targetFlow ->
                viewModelScope.launch {
                    val ids = userRepository.getEventIdsWithStatus(status)
                    val events = ids.mapNotNull { id ->
                        eventRepository.getEventById(id).fold(
                            onSuccess = { mapDomainToAppEvent(it) },
                            onFailure = { SampleData.events.find { e -> e.id == id } }
                        )
                    }
                    targetFlow.value = events
                }
            }
        }
    }

    private fun loadFavoriteEvents() {
        viewModelScope.launch {
            val ids = eventRepository.getFavoriteIds()
            val events = ids.mapNotNull { id ->
                eventRepository.getEventById(id).fold(
                    onSuccess = { mapDomainToAppEvent(it) },
                    onFailure = { SampleData.events.find { e -> e.id == id } }
                )
            }
            _favoriteEvents.value = events
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
            address = domainEvent.address,
            date = domainEvent.date.toString(),
            price = domainEvent.price,
            imageUrl = domainEvent.imageUrl,
            tags = domainEvent.tags,
            isFeatured = domainEvent.isFeatured
        )
    }

    fun logout() {
        viewModelScope.launch {
            userRepository.clearUserProfile()
        }
    }
}
