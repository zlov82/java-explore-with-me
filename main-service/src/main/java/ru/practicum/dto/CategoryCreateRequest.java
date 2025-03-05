package ru.practicum.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CategoryCreateRequest {
    @NotBlank
    @Size(max = 50, message = "Длина категории не более 50 символов")
    private String name;
}
