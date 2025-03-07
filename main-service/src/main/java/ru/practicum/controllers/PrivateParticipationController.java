package ru.practicum.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.ParticipationDto;
import ru.practicum.mappers.ParticipationMapper;
import ru.practicum.models.Participation;
import ru.practicum.services.ParticipationService;

@RestController
@RequestMapping("/users/{userId}/requests")
@RequiredArgsConstructor
public class PrivateParticipationController {
    private final ParticipationService service;
    private final ParticipationMapper mapper;

    @PostMapping
    public ResponseEntity<ParticipationDto> create(@PathVariable long userId,
                                                   @RequestParam long eventId) {
        Participation participation = service.create(userId, eventId);
        return new ResponseEntity<>(mapper.toDto(participation), HttpStatus.CREATED);
    }

    @PatchMapping("/{requestId}/cancel")
    private ResponseEntity<ParticipationDto> cancel(@PathVariable long userId,
                                                    @PathVariable long requestId) {
        Participation participation = service.cancel(userId, requestId);
        return new ResponseEntity<>(mapper.toDto(participation), HttpStatus.OK);
    }

}
