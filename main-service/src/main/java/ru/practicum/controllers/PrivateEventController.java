package ru.practicum.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.EventCreateRequest;
import ru.practicum.dto.EventDto;
import ru.practicum.dto.EventUpdateRequest;
import ru.practicum.mappers.EventMapper;
import ru.practicum.models.Event;
import ru.practicum.services.EventService;

import java.util.List;

@RestController
@RequestMapping("/users/{userId}/events")
@RequiredArgsConstructor
public class PrivateEventController {

    private final EventService service;
    private final EventMapper mapper;

    @GetMapping
    public ResponseEntity<List<EventDto>> getByUser(@PathVariable long userId,
                                                    @RequestParam(required = false, defaultValue = "0") int from,
                                                    @RequestParam(required = false, defaultValue = "10") int size) {

        List<Event> events = service.getPrivateEventsByUser(userId, from, size);

        return new ResponseEntity<>(mapper.toEventDto(events), HttpStatus.OK);
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<EventDto> getById(@PathVariable long eventId,
                                            @PathVariable long userId) {
        Event event = service.getPrivateEventById(userId, eventId);
        return new ResponseEntity<>(mapper.toEventDto(event), HttpStatus.OK);
    }

    @GetMapping("/{eventId}/requests")
    public ResponseEntity<Void> getXXX(@PathVariable long userId,
                                       @PathVariable long eventId) {
        //todo TBD -> Получение информации о запросах на участие в событии текущего пользователя

        return new ResponseEntity<>(HttpStatus.OK);
    }


    @PostMapping
    public ResponseEntity<EventDto> create(@PathVariable long userId,
                                           @Valid @RequestBody EventCreateRequest request) {
        Event event = service.createEvent(request, userId);
        return new ResponseEntity<>(mapper.toEventDto(event), HttpStatus.CREATED);
    }

    @PatchMapping("/{eventId}")
    private ResponseEntity<EventDto> update(@PathVariable long userId,
                                            @PathVariable long eventId,
                                            @RequestBody EventUpdateRequest request) {
        Event event = service.updateEventByUser(userId, eventId, request);
        return new ResponseEntity<>(mapper.toEventDto(event), HttpStatus.OK);
    }

    @PatchMapping("/{eventId}/requests")
    private ResponseEntity<Void> updateXXX(@PathVariable long userId,
                                           @PathVariable long eventId) {

        //todo TBD -> Изменение статуса заявков на участие в собитии текущего пользователя

        return new ResponseEntity<>(HttpStatus.OK);
    }

}
