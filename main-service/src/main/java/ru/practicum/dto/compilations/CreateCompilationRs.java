package ru.practicum.dto.compilations;

import lombok.Data;
import ru.practicum.models.Event;

import java.util.List;

@Data
public class CreateCompilationRs {
    private List<Event> events;
    private Long id;
    private Boolean pinned;
    private String title;
}
