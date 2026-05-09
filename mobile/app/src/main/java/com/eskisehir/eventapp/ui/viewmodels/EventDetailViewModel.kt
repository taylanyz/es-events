package com.eskisehir.eventapp.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eskisehir.eventapp.data.model.Event
import com.eskisehir.eventapp.data.model.SampleData
import com.eskisehir.events.data.local.entity.CommentEntity
import com.eskisehir.events.data.local.entity.EventStatus
import com.eskisehir.events.domain.repository.EventRepository
import com.eskisehir.events.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EventDetailViewModel @Inject constructor(
    private val eventRepository: EventRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _event = MutableStateFlow<Event?>(null)
    val event: StateFlow<Event?> = _event.asStateFlow()

    private val _isFavorite = MutableStateFlow(false)
    val isFavorite: StateFlow<Boolean> = _isFavorite.asStateFlow()

    private val _status = MutableStateFlow(EventStatus.NONE)
    val status: StateFlow<EventStatus> = _status.asStateFlow()

    private val _userProfile = userRepository.getUserProfile()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    fun loadEvent(eventId: Long) {
        viewModelScope.launch {
            // Load event data
            eventRepository.getEventById(eventId).fold(
                onSuccess = { domainEvent ->
                    _event.value = Event(
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
                },
                onFailure = {
                    // Fallback to sample data if API fails
                    _event.value = SampleData.events.find { it.id == eventId }
                }
            )
        }

        // Load favorite status and status in parallel (non-blocking)
        viewModelScope.launch {
            try {
                _isFavorite.value = eventRepository.isFavorite(eventId)
            } catch (e: Exception) {
                _isFavorite.value = false
            }
        }

        // Listen to status changes separately
        viewModelScope.launch {
            userRepository.getStatusForEvent(eventId).collect {
                _status.value = it
            }
        }
    }

    fun getComments(eventId: Long): Flow<List<CommentEntity>> = userRepository.getComments(eventId)

    fun addComment(eventId: Long, content: String) {
        val profile = _userProfile.value
        viewModelScope.launch {
            userRepository.addComment(
                CommentEntity(
                    eventId = eventId,
                    userEmail = profile?.email ?: "anonim@email.com",
                    userName = profile?.fullName ?: "Anonim Kullanıcı",
                    content = content
                )
            )
        }
    }

    fun toggleFavorite(eventId: Long) {
        viewModelScope.launch {
            eventRepository.toggleFavorite(eventId)
            _isFavorite.value = eventRepository.isFavorite(eventId)
        }
    }

    fun setStatus(eventId: Long, newStatus: EventStatus) {
        viewModelScope.launch {
            userRepository.setEventStatus(eventId, newStatus)
            _status.value = newStatus
        }
    }
}
