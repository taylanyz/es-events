package com.eskisehir.eventapi.service;

import com.eskisehir.eventapi.domain.model.Category;
import com.eskisehir.eventapi.domain.model.Event;
import com.eskisehir.eventapi.dto.RecommendationRequest;
import com.eskisehir.eventapi.dto.RouteRequest;
import com.eskisehir.eventapi.repository.EventRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class RecommendationService {

    private static final Logger log = LoggerFactory.getLogger(RecommendationService.class);

    private final EventRepository eventRepository;

    public RecommendationService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    // Default Eskişehir city center coordinates
    private static final double DEFAULT_LAT = 39.7667;
    private static final double DEFAULT_LNG = 30.5256;

    /**
     * Generates personalized event recommendations based on user preferences.
     *
     * @param request contains preferred categories, tags, budget, and limit
     * @return list of recommended events sorted by relevance score
     */
    public List<Event> getRecommendations(RecommendationRequest request) {
        List<Event> allEvents = eventRepository.findByDateAfter(LocalDateTime.now());
        
        log.info("Generating recommendations from {} upcoming events", allEvents.size());

        // Score each event based on user preferences
        Map<Event, Integer> scores = new HashMap<>();
        
        for (Event event : allEvents) {
            int score = calculateScore(event, request);
            scores.put(event, score);
        }

        // Sort by score descending, then by date ascending as tiebreaker
        return scores.entrySet().stream()
                .filter(entry -> entry.getValue() > 0) // Only include events with positive score
                .sorted((a, b) -> {
                    int scoreCompare = b.getValue().compareTo(a.getValue());
                    if (scoreCompare != 0) return scoreCompare;
                    return a.getKey().getDate().compareTo(b.getKey().getDate());
                })
                .limit(request.getEffectiveLimit())
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    /**
     * Calculates the recommendation score for a single event.
     * 
     * Scoring breakdown:
     *   - Category match: +3 points
     *   - Each matching tag: +1 point
     *   - Within 7 days: +1 point
     *   - Within budget: +1 point
     *
     * @param event   the event to score
     * @param request the user's preferences
     * @return total score for this event
     */
    private int calculateScore(Event event, RecommendationRequest request) {
        int score = 0;

        // Rule 1: Category match → +3 points
        if (request.getPreferredCategories() != null &&
            request.getPreferredCategories().contains(event.getCategory())) {
            score += 3;
            log.debug("Event '{}': +3 (category match: {})", event.getName(), event.getCategory());
        }

        // Rule 2: Tag overlap → +1 point per matching tag
        if (request.getPreferredTags() != null && event.getTags() != null) {
            long matchingTags = event.getTags().stream()
                    .filter(tag -> request.getPreferredTags().stream()
                            .anyMatch(prefTag -> prefTag.equalsIgnoreCase(tag)))
                    .count();
            score += (int) matchingTags;
            if (matchingTags > 0) {
                log.debug("Event '{}': +{} (tag matches)", event.getName(), matchingTags);
            }
        }

        // Rule 3: Recency bonus → +1 point if within next 7 days
        long daysUntilEvent = ChronoUnit.DAYS.between(LocalDateTime.now(), event.getDate());
        if (daysUntilEvent >= 0 && daysUntilEvent <= 7) {
            score += 1;
            log.debug("Event '{}': +1 (within 7 days)", event.getName());
        }

        // Rule 4: Price match → +1 point if within budget
        if (request.getMaxPrice() != null && event.getPrice() <= request.getMaxPrice()) {
            score += 1;
            log.debug("Event '{}': +1 (within budget)", event.getName());
        }

        return score;
    }

    /**
     * Orders events using a nearest-neighbor heuristic for route planning.
     * 
     * Algorithm:
     *   1. Start from the user's location or city center
     *   2. Always pick the closest unvisited event next
     *   3. Repeat until all events are ordered
     *
     * @param request contains event IDs and optional start location
     * @return events in optimized visiting order
     */
    public List<Event> planRoute(RouteRequest request) {
        // Fetch all requested events
        List<Event> events = request.getEventIds().stream()
                .map(id -> eventRepository.findById(id))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());

        if (events.size() <= 1) {
            return events;
        }

        // Starting point: user's location or Eskişehir city center
        double currentLat = request.getStartLatitude() != null ? request.getStartLatitude() : DEFAULT_LAT;
        double currentLng = request.getStartLongitude() != null ? request.getStartLongitude() : DEFAULT_LNG;

        // Nearest-neighbor greedy algorithm
        List<Event> orderedRoute = new ArrayList<>();
        Set<Long> visited = new HashSet<>();

        while (orderedRoute.size() < events.size()) {
            Event nearest = null;
            double nearestDistance = Double.MAX_VALUE;

            for (Event event : events) {
                if (visited.contains(event.getId())) continue;

                double distance = haversineDistance(currentLat, currentLng,
                        event.getLatitude(), event.getLongitude());

                if (distance < nearestDistance) {
                    nearestDistance = distance;
                    nearest = event;
                }
            }

            if (nearest != null) {
                orderedRoute.add(nearest);
                visited.add(nearest.getId());
                currentLat = nearest.getLatitude();
                currentLng = nearest.getLongitude();
            }
        }

        log.info("Route planned for {} events using nearest-neighbor heuristic", orderedRoute.size());
        return orderedRoute;
    }

    /**
     * Calculates the Haversine distance between two geographic points.
     * 
     * The Haversine formula determines the great-circle distance between
     * two points on a sphere given their longitudes and latitudes.
     * This is more accurate than Euclidean distance for geographic coordinates.
     *
     * @return distance in kilometers
     */
    private double haversineDistance(double lat1, double lon1, double lat2, double lon2) {
        final double R = 6371.0; // Earth's radius in kilometers

        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                   Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                   Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return R * c;
    }
}
