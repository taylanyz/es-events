package com.eskisehir.eventapi.service;

import com.eskisehir.eventapi.domain.model.Event;
import com.eskisehir.eventapi.dto.RecommendationResponseDto;
import com.eskisehir.eventapi.repository.EventRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class SmartRecommendationService {

    private final EmbeddingService embeddingService;
    private final EventRepository eventRepository;
    private final RecommendationService recommendationService;

    public SmartRecommendationService(
            EmbeddingService embeddingService,
            EventRepository eventRepository,
            RecommendationService recommendationService) {
        this.embeddingService = embeddingService;
        this.eventRepository = eventRepository;
        this.recommendationService = recommendationService;
    }

    public List<RecommendationResponseDto> getSmartRecommendations(
            String userInput,
            Long userId,
            int limit) {

        // 1️⃣ Get user preference embedding
        float[] userEmbedding = embeddingService.getEmbedding(userInput);

        // 2️⃣ Get all events
        List<Event> allEvents = eventRepository.findAll();

        // 3️⃣ Calculate scores (semantic + Thompson)
        List<RecommendationScore> scores = allEvents.stream()
                .map(event -> {
                    // Semantic similarity score
                    String eventDescription = buildEventDescription(event);
                    float[] eventEmbedding = embeddingService.getEmbedding(eventDescription);
                    double semanticScore = embeddingService.cosineSimilarity(userEmbedding, eventEmbedding);

                    // Thompson Sampling score (from user interaction history)
                    double thompsonScore = recommendationService.getThompsonScore(event.getId(), userId);

                    // Weighted combination
                    double finalScore = (semanticScore * 0.6) + (thompsonScore * 0.4);

                    return new RecommendationScore(event, semanticScore, thompsonScore, finalScore);
                })
                .sorted((a, b) -> Double.compare(b.finalScore, a.finalScore))
                .collect(Collectors.toList());

        // 4️⃣ Convert to DTO and return top N
        return scores.stream()
                .limit(limit)
                .map(score -> new RecommendationResponseDto(
                        score.event.getId(),
                        score.event.getName(),
                        score.finalScore,
                        score.semanticScore,
                        score.thompsonScore,
                        buildExplanation(score.event, userInput),
                        score.event.getTags() != null ? score.event.getTags() : new ArrayList<>()
                ))
                .collect(Collectors.toList());
    }

    private String buildEventDescription(Event event) {
        return String.join(" ",
                event.getName(),
                event.getDescription(),
                event.getCategory().toString(),
                event.getEnvironmentType(),
                event.getDifficultyLevel(),
                event.getActivityLevel(),
                String.join(" ", event.getTags() != null ? event.getTags() : new ArrayList<>())
        );
    }

    private String buildExplanation(Event event, String userInput) {
        // Simple explanation based on event attributes
        StringBuilder explanation = new StringBuilder("Bu etkinlik sana uygun çünkü: ");

        // Match environment preference
        if (userInput.toLowerCase().contains("sessiz")) {
            if ("sessiz".equals(event.getEnvironmentType())) {
                explanation.append("sessiz ortam, ");
            }
        } else if (userInput.toLowerCase().contains("kalabalık")) {
            if ("kalabalık".equals(event.getEnvironmentType())) {
                explanation.append("sosyal ortam, ");
            }
        }

        // Match budget
        if (userInput.toLowerCase().contains("ucuz") || userInput.toLowerCase().contains("bütçe")) {
            if (event.getPrice() < 50) {
                explanation.append("uygun fiyat, ");
            }
        }

        // Match activity level
        if (userInput.toLowerCase().contains("aktif")) {
            if ("yoğun".equals(event.getActivityLevel())) {
                explanation.append("dinamik aktivite, ");
            }
        } else if (userInput.toLowerCase().contains("dinlenme")) {
            if ("pasif".equals(event.getActivityLevel())) {
                explanation.append("rahatlatıcı, ");
            }
        }

        // Match category keywords
        if (userInput.toLowerCase().contains("sanat")) {
            if (event.getTags() != null && event.getTags().contains("sanat")) {
                explanation.append("sanatsal, ");
            }
        }

        // Add duration
        explanation.append(event.getDuration()).append(" dakika");

        // Trim trailing comma if present
        String result = explanation.toString();
        if (result.endsWith(", ")) {
            result = result.substring(0, result.length() - 2);
        }

        return result;
    }

    private static class RecommendationScore {
        Event event;
        double semanticScore;
        double thompsonScore;
        double finalScore;

        RecommendationScore(Event event, double semanticScore, double thompsonScore, double finalScore) {
            this.event = event;
            this.semanticScore = semanticScore;
            this.thompsonScore = thompsonScore;
            this.finalScore = finalScore;
        }
    }
}
