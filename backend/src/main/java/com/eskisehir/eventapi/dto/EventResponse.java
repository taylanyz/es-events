package com.eskisehir.eventapi.dto;

import com.eskisehir.eventapi.domain.model.Category;
import com.eskisehir.eventapi.domain.model.Event;
import java.time.LocalDateTime;
import java.util.List;

public class EventResponse {

    private Long id;
    private String name;
    private String description;
    private Category category;
    private Double latitude;
    private Double longitude;
    private String venue;
    private LocalDateTime date;
    private Double price;
    private String imageUrl;
    private List<String> tags;

    public EventResponse() {}

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

    public static EventResponse fromEntity(Event event) {
        EventResponse response = new EventResponse();
        response.setId(event.getId());
        response.setName(event.getName());
        response.setDescription(event.getDescription());
        response.setCategory(event.getCategory());
        response.setLatitude(event.getLatitude());
        response.setLongitude(event.getLongitude());
        response.setVenue(event.getVenue());
        response.setDate(event.getDate());
        response.setPrice(event.getPrice());
        response.setImageUrl(event.getImageUrl());
        response.setTags(event.getTags());
        return response;
    }
}
