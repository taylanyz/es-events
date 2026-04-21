package com.eskisehir.eventapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main entry point for the Eskişehir Event API application.
 * 
 * This Spring Boot application serves as the backend for an AI-powered
 * event recommendation system focused on Eskişehir city.
 */
@SpringBootApplication
public class EventApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(EventApiApplication.class, args);
    }
}
