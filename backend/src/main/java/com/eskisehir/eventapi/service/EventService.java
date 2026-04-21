package com.eskisehir.eventapi.service;

import com.eskisehir.eventapi.domain.model.Category;
import com.eskisehir.eventapi.domain.model.Event;
import com.eskisehir.eventapi.exception.EventNotFoundException;
import com.eskisehir.eventapi.repository.EventRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EventService {

    private final EventRepository eventRepository;

    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    /**
     * Retrieves all events, ordered by date ascending.
     *
     * @return list of all events sorted by nearest date first
     */
    public List<Event> getAllEvents() {
        return eventRepository.findAllByOrderByDateAsc();
    }

    /**
     * Retrieves a single event by its ID.
     *
     * @param id the event ID
     * @return the event entity
     * @throws EventNotFoundException if no event exists with the given ID
     */
    public Event getEventById(Long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new EventNotFoundException(id));
    }

    /**
     * Retrieves events filtered by category.
     *
     * @param category the category to filter by
     * @return list of events in the given category
     */
    public List<Event> getEventsByCategory(Category category) {
        return eventRepository.findByCategory(category);
    }

    /**
     * Retrieves only upcoming events (date > now).
     *
     * @return list of upcoming events
     */
    public List<Event> getUpcomingEvents() {
        return eventRepository.findByDateAfter(LocalDateTime.now());
    }

    /**
     * Searches events by a query string across name, description, and venue.
     *
     * @param query the search term
     * @return list of matching events
     */
    public List<Event> searchEvents(String query) {
        if (query == null || query.trim().isEmpty()) {
            return getAllEvents();
        }
        return eventRepository.searchEvents(query.trim());
    }

    /**
     * Retrieves upcoming events filtered by category.
     *
     * @param category the category to filter by
     * @return list of upcoming events in the given category
     */
    public List<Event> getUpcomingEventsByCategory(Category category) {
        return eventRepository.findByCategoryAndDateAfter(category, LocalDateTime.now());
    }
}
