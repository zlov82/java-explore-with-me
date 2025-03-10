package ru.practicum.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.dto.compilations.CreateCompilationRq;
import ru.practicum.dto.compilations.CreateCompilationRs;
import ru.practicum.models.Compilation;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CompilationMapper {
    CreateCompilationRs toDto(Compilation compilation);

    List<CreateCompilationRs> toDto(List<Compilation> compilations);

    @Mapping(target = "events", ignore = true)
    Compilation toCompilation(CreateCompilationRq request);
}
