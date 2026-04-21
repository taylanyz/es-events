package com.eskisehir.eventapi.config;

import com.eskisehir.eventapi.domain.model.Category;
import com.eskisehir.eventapi.domain.model.Event;
import com.eskisehir.eventapi.repository.EventRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

@Configuration
public class DataSeeder implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataSeeder.class);

    private final EventRepository eventRepository;

    public DataSeeder(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // Only seed if database is empty
        if (eventRepository.count() > 0) {
            log.info("Database already contains {} events, skipping seed.", eventRepository.count());
            return;
        }

        log.info("Seeding database with mock events...");

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        try {
            InputStream inputStream = new ClassPathResource("data/events.json").getInputStream();
            List<Event> events = mapper.readValue(inputStream, new TypeReference<List<Event>>() {});
            
            eventRepository.saveAll(events);
            log.info("Successfully seeded {} events into the database.", events.size());
        } catch (Exception e) {
            log.error("Failed to seed database: {}", e.getMessage());
            throw e;
        }
    }
}
