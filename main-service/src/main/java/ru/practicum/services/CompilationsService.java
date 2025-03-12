package ru.practicum.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.dto.compilations.CreateCompilationRq;
import ru.practicum.dto.compilations.UpdateCompilationRq;
import ru.practicum.exceptions.NotFoundException;
import ru.practicum.mappers.CompilationMapper;
import ru.practicum.models.Compilation;
import ru.practicum.models.Event;
import ru.practicum.repository.CompilationRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CompilationsService {
    private final EventService eventService;
    private final CompilationRepository repository;
    private final CompilationMapper mapper;

    public Compilation create(CreateCompilationRq request) {
        Compilation compilation = mapper.toCompilation(request);
        compilation.setEvents(eventService.getEventsByIds(request.getEvents()));
        if (request.getPinned() == null) {
            compilation.setPinned(false);
        }
        return repository.save(compilation);
    }

    public Compilation update(long id, UpdateCompilationRq request) {
        Compilation compilation = this.getById(id);

        if (request.getPinned() != null) {
            compilation.setPinned(request.getPinned());
        }

        if (request.getTitle() != null) {
            compilation.setTitle(request.getTitle());
        }

        if (request.getEvents() != null) {
            List<Event> requestEvents = eventService.getEventsByIds(request.getEvents());
            compilation.setEvents(requestEvents);
        }

        return repository.save(compilation);
    }

    public void deleteById(long id) {
        Compilation compilation = this.getById(id);
        repository.delete(compilation);
    }

    public Compilation getById(long id) {
        return repository.findById(id).orElseThrow(
                () -> new NotFoundException("Не найдена подбробка с id=" + id)
        );
    }

    public List<Compilation> getByFilter(Boolean pinned, Integer from, Integer size) {
        if (pinned == null) {
            return repository.findAllWithFromAndSize(from, size);
        }
        return repository.findByPinnedWithFromAndSize(pinned, from, size);
    }

}
