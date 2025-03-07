package ru.practicum.dto.event;

import lombok.Data;
import ru.practicum.models.Event;

@Data
public class EventDbResponse {
    private Event event;
    private Long confirmedRequests;

    public EventDbResponse(Event event, Long confirmedRequests) {
        this.event = event;
        this.confirmedRequests = confirmedRequests;
    }
}
