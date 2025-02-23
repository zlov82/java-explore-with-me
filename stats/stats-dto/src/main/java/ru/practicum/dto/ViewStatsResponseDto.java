package ru.practicum.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "Ответ статистики")
public class ViewStatsResponseDto {
    @Schema(description = "Название сервиса", example = "ewm-main-service")
    private String app;

    @Schema(description = "URI сервиса", example = "/events/1")
    private String uri;

    @Schema(description = "Количество просмотров", example = "6")
    private Long hits;
}
