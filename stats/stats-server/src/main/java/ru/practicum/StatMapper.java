package ru.practicum;

import ru.practicum.dto.HitRequestDto;
import ru.practicum.dto.ViewStatsResponseDto;
import ru.practicum.models.Hit;
import ru.practicum.models.StatShortResponse;

public class StatMapper {
    public static Hit toStatistic(HitRequestDto requestDto) {
        return Hit.builder()
                .app(requestDto.getApp())
                .ip(requestDto.getIp())
                .uri(requestDto.getUri())
                .localDateTime(requestDto.getTimestamp())
                .build();

    }

    public static ViewStatsResponseDto toStatsResponseDto(StatShortResponse hit) {
        return ViewStatsResponseDto.builder()
                .app(hit.getApp())
                .uri(hit.getUri())
                .hits(hit.getViews())
                .build();
    }
}
