package com.eskisehir.eventapi.domain.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "events")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(length = 1000)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Category category;

    @Column(nullable = false)
    private Double latitude;

    @Column(nullable = false)
    private Double longitude;

    @Column(nullable = false)
    private String venue;

    @Column(nullable = false)
    private LocalDateTime date;

    @Column(nullable = false)
    private Double price;

    private String imageUrl;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "event_tags", joinColumns = @JoinColumn(name = "event_id"))
    @Column(name = "tag")
    private List<String> tags;

    @Column(nullable = false)
    private String environmentType;  // "kalabalık", "sessiz", "karma"

    @Column(nullable = false)
    private String difficultyLevel;  // "başlangıç", "orta", "ileri"

    @Column(nullable = false)
    private String groupSizeType;    // "solo", "çift", "grup", "her biri"

    @Column(nullable = false)
    private String activityLevel;    // "pasif", "hafif", "orta", "yoğun"

    @Column(nullable = false)
    private String socialAspect;     // "yalnız", "arkadaş", "yeni insanlar"

    @Column(nullable = false)
    private Boolean isWheelchairAccessible;

    @Column(nullable = false)
    private Boolean hasParking;

    @Column(nullable = false)
    private Boolean hasPublicTransport;

    @Column(nullable = false)
    private Boolean allowsPhotography;

    @Column(nullable = false)
    private Boolean hasFoodDrink;

    private String language;  // "TR", "ENG", null

    @Column(nullable = false)
    private Integer duration;  // minutes

    @Column(nullable = false)
    private Boolean isIndoor;  // true = kapalı, false = açık hava

    private String ageGroup;  // "Çocuk", "Genç", "Yetişkin", "Yaşlı" (nullable = user doesn't care)

    private String culturalValue;  // "Hiç", "Biraz", "Çok" (nullable)

    @Column(nullable = false)
    private Boolean weatherDependent;  // true = hava bağımlı, false = bağımsız

    private String bestTimeOfDay;  // "Sabah", "Öğle", "Akşam", "Gece" (nullable)

    private String crowdSize;  // "Çok az", "Az", "Orta", "Çok" (nullable)

    public Event() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }
    public Double getLatitude() { return latitude; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }
    public Double getLongitude() { return longitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }
    public String getVenue() { return venue; }
    public void setVenue(String venue) { this.venue = venue; }
    public LocalDateTime getDate() { return date; }
    public void setDate(LocalDateTime date) { this.date = date; }
    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public List<String> getTags() { return tags; }
    public void setTags(List<String> tags) { this.tags = tags; }
    public String getEnvironmentType() { return environmentType; }
    public void setEnvironmentType(String environmentType) { this.environmentType = environmentType; }
    public String getDifficultyLevel() { return difficultyLevel; }
    public void setDifficultyLevel(String difficultyLevel) { this.difficultyLevel = difficultyLevel; }
    public String getGroupSizeType() { return groupSizeType; }
    public void setGroupSizeType(String groupSizeType) { this.groupSizeType = groupSizeType; }
    public String getActivityLevel() { return activityLevel; }
    public void setActivityLevel(String activityLevel) { this.activityLevel = activityLevel; }
    public String getSocialAspect() { return socialAspect; }
    public void setSocialAspect(String socialAspect) { this.socialAspect = socialAspect; }
    public Boolean getIsWheelchairAccessible() { return isWheelchairAccessible; }
    public void setIsWheelchairAccessible(Boolean wheelchairAccessible) { isWheelchairAccessible = wheelchairAccessible; }
    public Boolean getHasParking() { return hasParking; }
    public void setHasParking(Boolean hasParking) { this.hasParking = hasParking; }
    public Boolean getHasPublicTransport() { return hasPublicTransport; }
    public void setHasPublicTransport(Boolean hasPublicTransport) { this.hasPublicTransport = hasPublicTransport; }
    public Boolean getAllowsPhotography() { return allowsPhotography; }
    public void setAllowsPhotography(Boolean allowsPhotography) { this.allowsPhotography = allowsPhotography; }
    public Boolean getHasFoodDrink() { return hasFoodDrink; }
    public void setHasFoodDrink(Boolean hasFoodDrink) { this.hasFoodDrink = hasFoodDrink; }
    public String getLanguage() { return language; }
    public void setLanguage(String language) { this.language = language; }
    public Integer getDuration() { return duration; }
    public void setDuration(Integer duration) { this.duration = duration; }
    public Boolean getIsIndoor() { return isIndoor; }
    public void setIsIndoor(Boolean isIndoor) { this.isIndoor = isIndoor; }
    public String getAgeGroup() { return ageGroup; }
    public void setAgeGroup(String ageGroup) { this.ageGroup = ageGroup; }
    public String getCulturalValue() { return culturalValue; }
    public void setCulturalValue(String culturalValue) { this.culturalValue = culturalValue; }
    public Boolean getWeatherDependent() { return weatherDependent; }
    public void setWeatherDependent(Boolean weatherDependent) { this.weatherDependent = weatherDependent; }
    public String getBestTimeOfDay() { return bestTimeOfDay; }
    public void setBestTimeOfDay(String bestTimeOfDay) { this.bestTimeOfDay = bestTimeOfDay; }
    public String getCrowdSize() { return crowdSize; }
    public void setCrowdSize(String crowdSize) { this.crowdSize = crowdSize; }
}
