package com.eskisehir.eventapi.dto;

import java.util.List;

public class RecommendationResponseDto {
    private Long eventId;
    private String eventName;
    private Double finalScore;
    private Double semanticScore;
    private Double thompsonScore;
    private String explanation;
    private List<String> tags;

    public RecommendationResponseDto(
            Long eventId,
            String eventName,
            Double finalScore,
            Double semanticScore,
            Double thompsonScore,
            String explanation,
            List<String> tags) {
        this.eventId = eventId;
        this.eventName = eventName;
        this.finalScore = finalScore;
        this.semanticScore = semanticScore;
        this.thompsonScore = thompsonScore;
        this.explanation = explanation;
        this.tags = tags;
    }

    public Long getEventId() { return eventId; }
    public void setEventId(Long eventId) { this.eventId = eventId; }

    public String getEventName() { return eventName; }
    public void setEventName(String eventName) { this.eventName = eventName; }

    public Double getFinalScore() { return finalScore; }
    public void setFinalScore(Double finalScore) { this.finalScore = finalScore; }

    public Double getSemanticScore() { return semanticScore; }
    public void setSemanticScore(Double semanticScore) { this.semanticScore = semanticScore; }

    public Double getThompsonScore() { return thompsonScore; }
    public void setThompsonScore(Double thompsonScore) { this.thompsonScore = thompsonScore; }

    public String getExplanation() { return explanation; }
    public void setExplanation(String explanation) { this.explanation = explanation; }

    public List<String> getTags() { return tags; }
    public void setTags(List<String> tags) { this.tags = tags; }
}
