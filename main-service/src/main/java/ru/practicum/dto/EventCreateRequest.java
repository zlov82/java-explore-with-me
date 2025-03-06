package ru.practicum.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EventCreateRequest {

    @NotBlank
    @Size(min = 3, max = 120, message = "Заголовок от 3 до 120 символов")
    private String title;

    @NotBlank
    @Size(min = 20, max = 2000, message = "Аннотация от 20 до 2000 символов")
    private String annotation;

    @Positive
    private Integer category;

    @NotBlank
    @Size(min = 20, max = 7000, message = "Описание от 20 до 7000 символов")
    private String description;

    @Future
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;

    private LocationDto location;
    private Boolean paid = false;
    @PositiveOrZero
    private Integer participantLimit = 0;
    private Boolean requestModeration = true;
}
