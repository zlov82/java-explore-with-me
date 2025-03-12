package ru.practicum.mappers;

import org.mapstruct.Mapper;
import ru.practicum.dto.user.UserCreateRequest;
import ru.practicum.dto.user.UserCreateResponse;
import ru.practicum.dto.user.UserShortDto;
import ru.practicum.models.User;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserCreateRequest userCreateRequest);

    UserCreateResponse toUserCreateResponse(User newUser);

    List<UserCreateResponse> toUserCreateResponse(List<User> users);

    UserShortDto toShortDto(User user);

    List<UserShortDto> toShortDto(List<User> userList);
}
