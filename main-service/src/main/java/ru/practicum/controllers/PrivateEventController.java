package ru.practicum.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.ParticipationDto;
import ru.practicum.dto.ParticipationUpdateByEventOwner;
import ru.practicum.dto.ParticipationUpdateByEventOwnerRs;
import ru.practicum.dto.event.EventCreateRequest;
import ru.practicum.dto.event.EventDto;
import ru.practicum.dto.event.EventUpdateRequest;
import ru.practicum.mappers.EventMapper;
import ru.practicum.mappers.ParticipationMapper;
import ru.practicum.models.Event;
import ru.practicum.models.Participation;
import ru.practicum.services.EventService;
import ru.practicum.services.ParticipationService;

import java.util.List;

@RestController
@RequestMapping("/users/{userId}/events")
@RequiredArgsConstructor
public class PrivateEventController {

    private final EventService service;
    private final EventMapper mapper;
    private final ParticipationMapper participationMapper;
    private final ParticipationService participationService;

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
    public ResponseEntity<List<ParticipationDto>> getParticipationByEventOwner(@PathVariable long userId,
                                                                               @PathVariable long eventId) {

        List<Participation> participationList = participationService.getUserEventRequest(userId, eventId);
        return new ResponseEntity<>(participationMapper.toDto(participationList), HttpStatus.OK);
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
                                            @Valid @RequestBody EventUpdateRequest request) {
        Event event = service.updateEventByUser(userId, eventId, request);
        return new ResponseEntity<>(mapper.toEventDto(event), HttpStatus.OK);
    }

    @PatchMapping("/{eventId}/requests")
    private ResponseEntity<ParticipationUpdateByEventOwnerRs> updateParticipationByEventOwner(@PathVariable long userId,
                                                                                              @PathVariable long eventId,
                                                                                              @Valid @RequestBody ParticipationUpdateByEventOwner request) {

        ParticipationUpdateByEventOwnerRs rs = participationService.updateParticipationByEventOwner(userId,
                eventId, request);

        return new ResponseEntity<>(rs, HttpStatus.OK);
    }

}
