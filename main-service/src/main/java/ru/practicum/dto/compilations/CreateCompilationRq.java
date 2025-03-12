package ru.practicum.dto.compilations;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Set;

@Data
public class CreateCompilationRq {
    private Set<Long> events;
    private Boolean pinned;

    @NotBlank
    @Size(max = 50)
    private String title;

}
