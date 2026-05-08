package com.eskisehir.eventapi.dto;

public class SmartRecommendationRequest {
    private Long userId;
    private Integer limit = 5;

    // Budget
    private Double maxPrice;
    private Double minPrice;

    // Duration
    private Integer minDuration;
    private Integer maxDuration;

    // Environment & Atmosphere
    private String environmentType;  // "kalabalık", "sessiz", "karma"
    private String crowdSize;        // "Çok az", "Az", "Orta", "Çok"
    private Boolean isIndoor;

    // Activity & Difficulty
    private String activityLevel;    // "pasif", "hafif", "orta", "yoğun"
    private String difficultyLevel;  // "başlangıç", "orta", "ileri"

    // Social & Group
    private String socialAspect;     // "yalnız", "arkadaş", "yeni insanlar"
    private String groupSize;        // "solo", "çift", "grup", "her biri"

    // Culture & Age
    private String ageGroup;         // "Çocuk", "Genç", "Yetişkin", "Yaşlı"
    private String culturalValue;    // "Hiç", "Biraz", "Çok"

    // Time & Weather
    private String bestTimeOfDay;    // "Sabah", "Öğle", "Akşam", "Gece"
    private Boolean weatherDependent;

    // Accessibility (all optional - only set true if required)
    private Boolean requireWheelchair;
    private Boolean requireParking;
    private Boolean requireTransport;
    private Boolean requirePhotography;
    private Boolean requireFood;

    public SmartRecommendationRequest() {}

    // Getters & Setters
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public Integer getLimit() { return limit; }
    public void setLimit(Integer limit) { this.limit = limit; }

    public Double getMaxPrice() { return maxPrice; }
    public void setMaxPrice(Double maxPrice) { this.maxPrice = maxPrice; }

    public Double getMinPrice() { return minPrice; }
    public void setMinPrice(Double minPrice) { this.minPrice = minPrice; }

    public Integer getMinDuration() { return minDuration; }
    public void setMinDuration(Integer minDuration) { this.minDuration = minDuration; }

    public Integer getMaxDuration() { return maxDuration; }
    public void setMaxDuration(Integer maxDuration) { this.maxDuration = maxDuration; }

    public String getEnvironmentType() { return environmentType; }
    public void setEnvironmentType(String environmentType) { this.environmentType = environmentType; }

    public String getCrowdSize() { return crowdSize; }
    public void setCrowdSize(String crowdSize) { this.crowdSize = crowdSize; }

    public Boolean getIsIndoor() { return isIndoor; }
    public void setIsIndoor(Boolean isIndoor) { this.isIndoor = isIndoor; }

    public String getActivityLevel() { return activityLevel; }
    public void setActivityLevel(String activityLevel) { this.activityLevel = activityLevel; }

    public String getDifficultyLevel() { return difficultyLevel; }
    public void setDifficultyLevel(String difficultyLevel) { this.difficultyLevel = difficultyLevel; }

    public String getSocialAspect() { return socialAspect; }
    public void setSocialAspect(String socialAspect) { this.socialAspect = socialAspect; }

    public String getGroupSize() { return groupSize; }
    public void setGroupSize(String groupSize) { this.groupSize = groupSize; }

    public String getAgeGroup() { return ageGroup; }
    public void setAgeGroup(String ageGroup) { this.ageGroup = ageGroup; }

    public String getCulturalValue() { return culturalValue; }
    public void setCulturalValue(String culturalValue) { this.culturalValue = culturalValue; }

    public String getBestTimeOfDay() { return bestTimeOfDay; }
    public void setBestTimeOfDay(String bestTimeOfDay) { this.bestTimeOfDay = bestTimeOfDay; }

    public Boolean getWeatherDependent() { return weatherDependent; }
    public void setWeatherDependent(Boolean weatherDependent) { this.weatherDependent = weatherDependent; }

    public Boolean getRequireWheelchair() { return requireWheelchair; }
    public void setRequireWheelchair(Boolean requireWheelchair) { this.requireWheelchair = requireWheelchair; }

    public Boolean getRequireParking() { return requireParking; }
    public void setRequireParking(Boolean requireParking) { this.requireParking = requireParking; }

    public Boolean getRequireTransport() { return requireTransport; }
    public void setRequireTransport(Boolean requireTransport) { this.requireTransport = requireTransport; }

    public Boolean getRequirePhotography() { return requirePhotography; }
    public void setRequirePhotography(Boolean requirePhotography) { this.requirePhotography = requirePhotography; }

    public Boolean getRequireFood() { return requireFood; }
    public void setRequireFood(Boolean requireFood) { this.requireFood = requireFood; }
}
