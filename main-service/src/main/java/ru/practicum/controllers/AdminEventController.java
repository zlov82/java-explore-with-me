package ru.practicum.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.event.EventDto;
import ru.practicum.dto.event.EventUpdateRequest;
import ru.practicum.mappers.EventMapper;
import ru.practicum.models.Event;
import ru.practicum.services.EventService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/admin/events")
@RequiredArgsConstructor
public class AdminEventController {

    private final EventService service;
    private final EventMapper mapper;

    @GetMapping
    public ResponseEntity<List<EventDto>> getEvenstByAdmin(@RequestParam(required = false) List<Long> users,
                                                           @RequestParam(required = false) List<String> states,
                                                           @RequestParam(required = false) List<Integer> categories,
                                                           @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
                                                           @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
                                                           @RequestParam(required = false, defaultValue = "0") int from,
                                                           @RequestParam(required = false, defaultValue = "10") int size) {

        List<Event> eventList = service.getEventsByAdmin(users, states, categories, rangeStart, rangeEnd, from, size);

        return new ResponseEntity<>(mapper.toEventDto(eventList), HttpStatus.OK);
    }

    @PatchMapping("/{eventId}")
    public ResponseEntity<EventDto> updateEventByAdmin(@PathVariable long eventId,
                                                       @RequestBody EventUpdateRequest request) {
        Event event = service.updateEventByAdmin(eventId, request);
        return new ResponseEntity<>(mapper.toEventDto(event), HttpStatus.OK);
    }
}
