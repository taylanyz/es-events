package com.eskisehir.eventapi.repository;

import com.eskisehir.eventapi.domain.model.Category;
import com.eskisehir.eventapi.domain.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Spring Data JPA repository for Event entities.
 * 
 * Provides built-in CRUD operations plus custom queries
 * for category filtering, search, and date-based lookups.
 */
@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    /**
     * Find all events matching a specific category.
     */
    List<Event> findByCategory(Category category);

    /**
     * Find all events occurring after a given date.
     * Used to filter upcoming events only.
     */
    List<Event> findByDateAfter(LocalDateTime date);

    /**
     * Find events whose category matches and that occur after the given date.
     * Combines category filtering with upcoming-only logic.
     */
    List<Event> findByCategoryAndDateAfter(Category category, LocalDateTime date);

    /**
     * Search events by name (case-insensitive partial match).
     */
    @Query("SELECT e FROM Event e WHERE LOWER(e.name) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<Event> searchByName(@Param("query") String query);

    /**
     * Full-text search across event name, description, and venue.
     * Used by the search/filter feature on the Explore screen.
     */
    @Query("SELECT e FROM Event e WHERE " +
           "LOWER(e.name) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(e.description) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(e.venue) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<Event> searchEvents(@Param("query") String query);

    /**
     * Find events ordered by date ascending (nearest first).
     */
    List<Event> findAllByOrderByDateAsc();
}
