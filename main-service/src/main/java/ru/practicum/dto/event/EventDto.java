package ru.practicum.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import ru.practicum.dto.CategoryCreateResponse;
import ru.practicum.dto.LocationDto;
import ru.practicum.dto.user.UserShortDto;

import java.time.LocalDateTime;

@Data
public class EventDto {
    private Long id;
    private String title;
    private String annotation;
    private String description;
    private Integer participantLimit;
    private CategoryCreateResponse category;
    private UserShortDto initiator;
    private Integer confirmedRequests;
    private LocationDto location;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdOn;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;

    private Boolean paid;
    private EventState state;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private LocalDateTime publishedOn;

    private Boolean requestModeration;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long views;
}
