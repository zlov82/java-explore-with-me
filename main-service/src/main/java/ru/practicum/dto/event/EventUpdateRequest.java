package ru.practicum.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Data;
import ru.practicum.dto.LocationDto;

import java.time.LocalDateTime;

@Data
public class EventUpdateRequest {

    @Size(min = 3, max = 120, message = "Заголовок от 3 до 120 символов")
    private String title;

    @Size(min = 20, max = 2000, message = "Аннотация от 20 до 2000 символов")
    private String annotation;

    @Positive
    private Integer category;

    @Size(min = 20, max = 7000, message = "Описание от 20 до 7000 символов")
    private String description;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @FutureOrPresent
    private LocalDateTime eventDate;

    private LocationDto location;
    private Boolean paid;

    @PositiveOrZero
    private Long participantLimit;

    private Boolean requestModeration;
    private StateAction stateAction;


}
