package ru.practicum.controllers;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.event.EventDto;
import ru.practicum.dto.event.EventSearch;
import ru.practicum.ewm.client.StatsClient;
import ru.practicum.mappers.EventMapper;
import ru.practicum.models.Event;
import ru.practicum.services.EventService;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class PublicEventController {
    private static final String APP = "main-service";

    private final EventService service;
    private final EventMapper mapper;
    private final StatsClient client;

    @GetMapping
    public ResponseEntity<List<EventDto>> getEvents(@RequestParam(required = false) String text,
                                                    @RequestParam(required = false) List<Integer> categories,
                                                    @RequestParam(required = false) Boolean paid,
                                                    @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
                                                    @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
                                                    @RequestParam(defaultValue = "false") Boolean onlyAvailable,
                                                    @RequestParam(defaultValue = "EVENT_DATE") EventSearch sort,
                                                    @RequestParam(defaultValue = "0") Integer from,
                                                    @RequestParam(defaultValue = "10") Integer size,
                                                    final HttpServletRequest httpRequest) {

        log.info("Public get events with params");
        client.hit(APP, httpRequest.getRequestURI(), httpRequest.getRemoteAddr(), LocalDateTime.now());
        List<Event> eventList = service.getPublicEvents(text, categories, paid, rangeStart, rangeEnd,
                onlyAvailable, sort, from, size);
        return new ResponseEntity<>(mapper.toEventDto(eventList), HttpStatus.OK);
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<EventDto> getEventById(@PathVariable Long eventId,
                                                 final HttpServletRequest httpRequest) {
        log.info("Public get eventId=" + eventId);
        Event event = service.getPublicEventById(eventId);
        client.hit(APP, httpRequest.getRequestURI(), httpRequest.getRemoteAddr(), LocalDateTime.now());
        return new ResponseEntity<>(mapper.toEventDto(event), HttpStatus.OK);
    }
}
