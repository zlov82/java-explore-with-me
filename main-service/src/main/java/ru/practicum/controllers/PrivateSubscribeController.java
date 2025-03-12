package ru.practicum.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.EventFollowerType;
import ru.practicum.dto.event.EventDto;
import ru.practicum.dto.user.UserShortDto;
import ru.practicum.exceptions.BadRequestException;
import ru.practicum.mappers.EventMapper;
import ru.practicum.mappers.UserMapper;
import ru.practicum.models.Event;
import ru.practicum.models.Subscribe;
import ru.practicum.models.User;
import ru.practicum.services.EventService;
import ru.practicum.services.SubscribeService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/users/{userId}/subscriptions")
@RequiredArgsConstructor
public class PrivateSubscribeController {
    private final SubscribeService service;
    private final UserMapper userMapper;
    private final EventService eventService;
    private final EventMapper eventMapper;

    @PostMapping("/{followerId}")
    public ResponseEntity<UserShortDto> createSubscribe(@PathVariable Long userId,
                                                        @PathVariable Long followerId) {
        Subscribe subscribe = service.create(userId, followerId);
        return new ResponseEntity<>(userMapper.toShortDto(subscribe.getFollower()), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<UserShortDto>> getAllSubscribe(@PathVariable Long userId) {
        List<Subscribe> subscribeList = service.getAll(userId);
        List<User> friendList = subscribeList.stream().map(Subscribe::getFollower).toList();

        return new ResponseEntity<>(userMapper.toShortDto(friendList), HttpStatus.OK);
    }

    @GetMapping("/events")
    public ResponseEntity<List<EventDto>> getFollowerEvents(@PathVariable Long userId,
                                                            @RequestParam(required = false, defaultValue = "AUTHOR") EventFollowerType type,
                                                            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
                                                            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
                                                            @RequestParam(required = false, defaultValue = "0") int from,
                                                            @RequestParam(required = false, defaultValue = "10") int size) {

        List<Subscribe> subscribeList = service.getAll(userId);
        if (subscribeList == null || subscribeList.isEmpty()) {
            return new ResponseEntity<>(List.of(), HttpStatus.OK);
        }

        List<User> userLists = subscribeList.stream().map(Subscribe::getFollower).toList();

        List<Event> events = new ArrayList<>();
        switch (type) {
            case AUTHOR -> {
                events = eventService.getFollowersEvents(userLists, rangeStart, rangeEnd, size, from);
            }
            case MEMBER -> {
                events = eventService.getMembersEvents(userLists, rangeStart, rangeEnd, size, from);
            }
            default -> {
                throw new BadRequestException("Тип категории не корректен");
            }
        }


        return new ResponseEntity<>(eventMapper.toEventDto(events), HttpStatus.OK);
    }

    @DeleteMapping("/{followerId}")
    public ResponseEntity<Void> delete(@PathVariable Long userId,
                                       @PathVariable Long followerId) {

        service.delete(userId, followerId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


}
