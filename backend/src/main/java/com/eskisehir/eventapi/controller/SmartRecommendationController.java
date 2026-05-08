package com.eskisehir.eventapi.controller;

import com.eskisehir.eventapi.dto.RecommendationResponseDto;
import com.eskisehir.eventapi.dto.SmartRecommendationRequest;
import com.eskisehir.eventapi.service.ClaudeRecommendationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/smart-recommendations")
@CrossOrigin(origins = "*")
public class SmartRecommendationController {

    private final ClaudeRecommendationService claudeRecommendationService;

    public SmartRecommendationController(ClaudeRecommendationService claudeRecommendationService) {
        this.claudeRecommendationService = claudeRecommendationService;
    }

    @PostMapping
    public ResponseEntity<List<RecommendationResponseDto>> getSmartRecommendations(
            @RequestBody SmartRecommendationRequest request) {

        List<RecommendationResponseDto> recommendations = claudeRecommendationService.getSmartRecommendations(request);

        return ResponseEntity.ok(recommendations);
    }
}
