package com.eskisehir.eventapi.dto;

import java.util.List;

public class RouteRequest {

    private List<Long> eventIds;
    private Double startLatitude;
    private Double startLongitude;

    public RouteRequest() {}

    public List<Long> getEventIds() { return eventIds; }
    public void setEventIds(List<Long> eventIds) { this.eventIds = eventIds; }
    public Double getStartLatitude() { return startLatitude; }
    public void setStartLatitude(Double startLatitude) { this.startLatitude = startLatitude; }
    public Double getStartLongitude() { return startLongitude; }
    public void setStartLongitude(Double startLongitude) { this.startLongitude = startLongitude; }
}
