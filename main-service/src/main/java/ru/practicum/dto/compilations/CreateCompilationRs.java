package ru.practicum.dto.compilations;

import lombok.Data;
import ru.practicum.dto.event.EventDto;

import java.util.List;

@Data
public class CreateCompilationRs {
    private List<EventDto> events;
    private Long id;
    private Boolean pinned;
    private String title;
}
