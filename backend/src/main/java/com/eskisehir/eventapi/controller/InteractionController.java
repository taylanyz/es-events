package com.eskisehir.eventapi.controller;

import com.eskisehir.eventapi.dto.InteractionRequest;
import com.eskisehir.eventapi.service.RecommendationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/interactions")
@CrossOrigin(origins = "*")
public class InteractionController {

    private final RecommendationService recommendationService;

    public InteractionController(RecommendationService recommendationService) {
        this.recommendationService = recommendationService;
    }

    @PostMapping
    public ResponseEntity<Void> logInteraction(@Valid @RequestBody InteractionRequest request) {
        recommendationService.recordInteraction(request.getEventId(), request.isClicked());
        return ResponseEntity.ok().build();
    }
}
