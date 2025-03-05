package ru.practicum.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class UserCreateRequest {
    @Schema(description = "Email клиента",
            example = "client1@mail.ru")
    @Email
    @NotBlank
    @Size(min = 6, max = 254, message = "длина email в интервале 6-254 символа")
    private String email;

    @Schema(description = "Имя клиента",
            example = "Иванов Иван Иванович")
    @NotBlank
    @Size(min = 2, max = 250, message = "Имя должно быть в интервале от 2 до 250 символов")
    private String name;
}
