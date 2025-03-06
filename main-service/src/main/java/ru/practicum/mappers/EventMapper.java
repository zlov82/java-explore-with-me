package ru.practicum.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.dto.EventCreateRequest;
import ru.practicum.dto.EventDto;
import ru.practicum.dto.LocationDto;
import ru.practicum.models.Event;
import ru.practicum.models.Location;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EventMapper {

    @Mapping(target = "category", ignore = true)
    Event toEvent(EventCreateRequest request);

    EventDto toEventDto(Event event);

    List<EventDto> toEventDto(List<Event> events);

    Location toLocation(LocationDto location);
}
