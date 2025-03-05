package ru.practicum.mappers;

import jakarta.validation.Valid;
import org.mapstruct.Mapper;
import ru.practicum.dto.UserCreateRequest;
import ru.practicum.dto.UserCreateResponse;
import ru.practicum.models.User;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserCreateRequest userCreateRequest);

    UserCreateResponse toUserCreateResponse(User newUser);

    List<UserCreateResponse> toUserCreateResponse(List<User> users);
}
