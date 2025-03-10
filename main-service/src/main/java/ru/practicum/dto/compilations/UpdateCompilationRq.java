package ru.practicum.dto.compilations;

import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Set;

@Data
public class UpdateCompilationRq {
    private Set<Long> events;
    private Boolean pinned;

    @Size(max = 50)
    private String title;
}
