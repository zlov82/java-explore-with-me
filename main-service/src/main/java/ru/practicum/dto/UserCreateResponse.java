package ru.practicum.dto;

import lombok.Data;

@Data
public class UserCreateResponse {
    private Long id;
    private String email;
    private String name;
}
