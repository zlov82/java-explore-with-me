package ru.practicum;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.dto.HitRequestDto;
import ru.practicum.dto.ViewStatsResponseDto;
import ru.practicum.models.Hit;
import ru.practicum.models.StatShortResponse;

@Mapper(componentModel = "spring")
public interface StatMapper {
    @Mapping(target = "localDateTime", source = "timestamp")
    Hit toStatistic(HitRequestDto requestDto);

    @Mapping(target = "hits", source = "views")
    ViewStatsResponseDto toStatsResponseDto(StatShortResponse hit);
}
