package com.eskisehir.eventapi.controller;

import com.eskisehir.eventapi.domain.model.Category;
import com.eskisehir.eventapi.dto.EventResponse;
import com.eskisehir.eventapi.dto.RecommendationRequest;
import com.eskisehir.eventapi.dto.RouteRequest;
import com.eskisehir.eventapi.service.EventService;
import com.eskisehir.eventapi.service.RecommendationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/events")
@CrossOrigin(origins = "*") // Allow requests from Android emulator
public class EventController {

    private final EventService eventService;
    private final RecommendationService recommendationService;

    public EventController(EventService eventService, RecommendationService recommendationService) {
        this.eventService = eventService;
        this.recommendationService = recommendationService;
    }

    /**
     * GET /api/events
     * Returns all events, optionally filtered by category query parameter.
     * 
     * @param category optional category filter (e.g., "CONCERT")
     * @return list of event DTOs
     */
    @GetMapping
    public ResponseEntity<List<EventResponse>> getAllEvents(
            @RequestParam(required = false) Category category) {
        
        List<EventResponse> events;
        
        if (category != null) {
            events = eventService.getEventsByCategory(category).stream()
                    .map(EventResponse::fromEntity)
                    .collect(Collectors.toList());
        } else {
            events = eventService.getAllEvents().stream()
                    .map(EventResponse::fromEntity)
                    .collect(Collectors.toList());
        }
        
        return ResponseEntity.ok(events);
    }

    /**
     * GET /api/events/{id}
     * Returns a single event by its ID.
     * 
     * @param id the event ID
     * @return event DTO or 404 if not found
     */
    @GetMapping("/{id}")
    public ResponseEntity<EventResponse> getEventById(@PathVariable Long id) {
        EventResponse event = EventResponse.fromEntity(eventService.getEventById(id));
        return ResponseEntity.ok(event);
    }

    /**
     * GET /api/events/category/{category}
     * Returns all events in a specific category.
     * 
     * @param category the category enum value (e.g., CONCERT, THEATER)
     * @return list of event DTOs in the given category
     */
    @GetMapping("/category/{category}")
    public ResponseEntity<List<EventResponse>> getEventsByCategory(
            @PathVariable Category category) {
        
        List<EventResponse> events = eventService.getEventsByCategory(category).stream()
                .map(EventResponse::fromEntity)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(events);
    }

    /**
     * GET /api/events/search?q=...
     * Searches events by name, description, and venue.
     * 
     * @param q the search query string
     * @return list of matching event DTOs
     */
    @GetMapping("/search")
    public ResponseEntity<List<EventResponse>> searchEvents(@RequestParam String q) {
        List<EventResponse> events = eventService.searchEvents(q).stream()
                .map(EventResponse::fromEntity)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(events);
    }

    /**
     * GET /api/events/upcoming
     * Returns only upcoming events (date > now).
     * 
     * @return list of upcoming event DTOs
     */
    @GetMapping("/upcoming")
    public ResponseEntity<List<EventResponse>> getUpcomingEvents() {
        List<EventResponse> events = eventService.getUpcomingEvents().stream()
                .map(EventResponse::fromEntity)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(events);
    }

    /**
     * POST /api/events/recommend
     * Returns personalized event recommendations based on user preferences.
     * 
     * Request body: { preferredCategories, preferredTags, maxPrice, limit }
     * 
     * @param request recommendation parameters
     * @return list of recommended event DTOs, sorted by relevance
     */
    @PostMapping("/recommend")
    public ResponseEntity<List<EventResponse>> getRecommendations(
            @Valid @RequestBody RecommendationRequest request) {
        
        List<EventResponse> recommendations = recommendationService
                .getRecommendations(request).stream()
                .map(EventResponse::fromEntity)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(recommendations);
    }

    /**
     * POST /api/events/route
     * Returns events in an optimized visiting order using nearest-neighbor heuristic.
     * 
     * Request body: { eventIds, startLatitude, startLongitude }
     * 
     * @param request route planning parameters
     * @return list of event DTOs in optimized order
     */
    @PostMapping("/route")
    public ResponseEntity<List<EventResponse>> planRoute(
            @Valid @RequestBody RouteRequest request) {
        
        List<EventResponse> route = recommendationService
                .planRoute(request).stream()
                .map(EventResponse::fromEntity)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(route);
    }
}
