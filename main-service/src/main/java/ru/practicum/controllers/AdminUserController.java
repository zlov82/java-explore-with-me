package ru.practicum.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.UserCreateRequest;
import ru.practicum.dto.UserCreateResponse;
import ru.practicum.mappers.UserMapper;
import ru.practicum.models.User;
import ru.practicum.services.UserService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/admin/users")
@RequiredArgsConstructor
@Tag(name = "Admin: Пользователи", description = "API для работы с пользователями")
public class AdminUserController {

    private final UserService service;
    private final UserMapper mapper;

    @Operation(summary = "Получение информации о пользователях")
    @GetMapping
    public ResponseEntity<List<UserCreateResponse>> getUsers(@Parameter(description = "id пользователей")
                                                             @RequestParam(required = false) List<Long> ids,

                                                             @Parameter(description = "Количество элементов, которые нужно пропустить")
                                                             @RequestParam(required = false, defaultValue = "0") Long from,
                                                             @RequestParam(required = false, defaultValue = "10") Long size) {
        List<User> users = service.getUsers(ids, from, size);
        log.info("Response getUsers: {}", users);
        return new ResponseEntity<>(mapper.toUserCreateResponse(users), HttpStatus.OK);
    }

    @Operation(summary = "Создание пользователя")
    @PostMapping()
    public ResponseEntity<UserCreateResponse> registerUser(@Valid @RequestBody UserCreateRequest userCreateRequest) {
        log.info("Request registerUser: {}", userCreateRequest);

        User newUser = service.registerUser(mapper.toUser(userCreateRequest));
        log.info("Response registerUser: {}", newUser);
        return new ResponseEntity<>(mapper.toUserCreateResponse(newUser), HttpStatus.CREATED);
    }

    @Operation(summary = "Удаление пользователя")
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable long userId) {
        service.deleteUser(userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
