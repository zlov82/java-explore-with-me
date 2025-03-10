package ru.practicum.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class LocationDto {
    @Schema(example = "55.754167")
    private double lat;

    @Schema(example = "37.62")
    private double lon;
}
