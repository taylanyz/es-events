package com.eskisehir.eventapi.dto;

public class InteractionRequest {
    private Long eventId;
    private boolean clicked;

    public InteractionRequest() {}
    
    public InteractionRequest(Long eventId, boolean clicked) {
        this.eventId = eventId;
        this.clicked = clicked;
    }

    public Long getEventId() { return eventId; }
    public void setEventId(Long eventId) { this.eventId = eventId; }
    public boolean isClicked() { return clicked; }
    public void setClicked(boolean clicked) { this.clicked = clicked; }
}
