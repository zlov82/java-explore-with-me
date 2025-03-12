package ru.practicum.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.dto.ParticipationDto;
import ru.practicum.models.Participation;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ParticipationMapper {

    @Mapping(target = "event", source = "event.id")
    @Mapping(target = "requester", source = "requester.id")
    ParticipationDto toDto(Participation participation);

    @Mapping(target = "event", source = "event.id")
    @Mapping(target = "requester", source = "requester.id")
    List<ParticipationDto> toDto(List<Participation> participationList);
}
