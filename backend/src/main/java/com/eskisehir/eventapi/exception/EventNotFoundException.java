package com.eskisehir.eventapi.exception;

/**
 * Exception thrown when an event with a given ID is not found in the database.
 * Handled globally by {@link GlobalExceptionHandler} to return a 404 response.
 */
public class EventNotFoundException extends RuntimeException {

    public EventNotFoundException(Long id) {
        super("Event not found with id: " + id);
    }
}
