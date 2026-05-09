package com.eskisehir.eventapp.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eskisehir.eventapp.data.model.Event
import com.eskisehir.eventapp.data.model.SampleData
import com.eskisehir.events.data.local.entity.EventStatus
import com.eskisehir.events.domain.repository.EventRepository
import com.eskisehir.events.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val eventRepository: EventRepository
) : ViewModel() {

    val userProfile = userRepository.getUserProfile()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val favoritePlaces = userRepository.getFavoritePlaces()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Refresh trigger for when events change
    private val _refreshTrigger = MutableSharedFlow<Unit>(replay = 0)

    val attendedEvents: StateFlow<List<Event>> = _refreshTrigger
        .onStart { emit(Unit) } // Emit on subscribe
        .flatMapLatest { loadEventsByStatusFlow(EventStatus.ATTENDED) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val goingEvents: StateFlow<List<Event>> = _refreshTrigger
        .onStart { emit(Unit) }
        .flatMapLatest { loadEventsByStatusFlow(EventStatus.GOING) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val wantToGoEvents: StateFlow<List<Event>> = _refreshTrigger
        .onStart { emit(Unit) }
        .flatMapLatest { loadEventsByStatusFlow(EventStatus.WANT_TO_GO) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val favoriteEvents: StateFlow<List<Event>> = _refreshTrigger
        .onStart { emit(Unit) }
        .flatMapLatest { loadFavoriteEventsFlow() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        // Trigger initial load
        refreshEvents()
    }

    private fun loadEventsByStatusFlow(status: EventStatus): Flow<List<Event>> = flow {
        // Get all events in one API call, then filter locally
        eventRepository.getEvents().fold(
            onSuccess = { allEvents ->
                val statusIds = userRepository.getEventIdsWithStatus(status)
                val filteredEvents = allEvents.filter { it.id in statusIds }
                    .map { mapDomainToAppEvent(it) }
                emit(filteredEvents)
            },
            onFailure = {
                // Fallback to sample data
                val statusIds = userRepository.getEventIdsWithStatus(status)
                val filtered = SampleData.events.filter { it.id in statusIds }
                emit(filtered)
            }
        )
    }

    private fun loadFavoriteEventsFlow(): Flow<List<Event>> = flow {
        // Get all events in one API call, then filter by favorites
        eventRepository.getEvents().fold(
            onSuccess = { allEvents ->
                val favoriteIds = eventRepository.getFavoriteIds()
                val filteredEvents = allEvents.filter { it.id in favoriteIds }
                    .map { mapDomainToAppEvent(it) }
                emit(filteredEvents)
            },
            onFailure = {
                // Fallback to sample data
                val favoriteIds = eventRepository.getFavoriteIds()
                val filtered = SampleData.events.filter { it.id in favoriteIds }
                emit(filtered)
            }
        )
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

    fun refreshEvents() {
        viewModelScope.launch {
            _refreshTrigger.emit(Unit)
        }
    }

    fun logout() {
        viewModelScope.launch {
            userRepository.clearUserProfile()
        }
    }
}
