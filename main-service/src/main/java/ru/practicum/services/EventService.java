package ru.practicum.services;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.dto.EventCreateRequest;
import ru.practicum.dto.EventState;
import ru.practicum.dto.EventUpdateRequest;
import ru.practicum.dto.StateAction;
import ru.practicum.exceptions.BadRequestException;
import ru.practicum.exceptions.ConflictException;
import ru.practicum.exceptions.NotFoundException;
import ru.practicum.mappers.EventMapper;
import ru.practicum.models.Category;
import ru.practicum.models.Event;
import ru.practicum.models.Location;
import ru.practicum.models.User;
import ru.practicum.repository.EventRepository;
import ru.practicum.repository.LocationRepository;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventService {

    private final UserService userService;
    private final CategoriesService categoriesService;
    private final EventRepository eventRepo;
    private final LocationRepository locationRepo;
    private final EventMapper eventMapper;

    @Transactional
    public Event createEvent(@Valid EventCreateRequest request, long userId) {

        log.info("create event {} by user {}", request, userId);
        Event event = eventMapper.toEvent(request);

        User author = userService.getUserById(userId);
        Category category = categoriesService.getCategoryById(request.getCategory());

        event.setInitiator(author);
        event.setCategory(category);

        locationRepo.save(event.getLocation());

        return eventRepo.save(event);
    }

    public List<Event> getPrivateEventsByUser(long userId, int from, int size) {
        User user = userService.getUserById(userId);
        return eventRepo.selectEvents(user, from, size);
    }

    public List<Event> getPublicEvents(String text, List<Integer> categories, Boolean paid, LocalDateTime rangeStart,
                                       LocalDateTime rangeEnd, Boolean onlyAvailable, EventService sort, Integer from, Integer size) {

        if (text != null) {
            text = text.toLowerCase().trim();
        }

        if (rangeStart != null && rangeEnd != null && rangeStart.isAfter(rangeEnd)) {
            throw new BadRequestException("rangeEnd до rangeStart");
        }

        if (rangeStart == null && rangeEnd == null) {
            rangeStart = LocalDateTime.now();
        }

        if (from < 0 || size <= 0) {
            return List.of();
        }

        List<Event> nonSortedEvents = eventRepo.findTextInEventsWithParams(text, categories, paid, rangeStart, rangeEnd);
        //todo обращение в статистику

        //todo вставить сортиорку и оффсет


        return nonSortedEvents;
    }

    public List<Event> getEventsByAdmin(List<Long> userIds, List<String> states, List<Integer> categoryIds,
                                        LocalDateTime rangeStart, LocalDateTime rangeEnd, int from, int size) {


        return eventRepo.findAllByParams(userIds, categoryIds, states, rangeStart, rangeEnd, size, from);
    }

    public Event getPublicEventById(long id) {
        return eventRepo.findByIdAndState(id, EventState.PUBLISHED).orElseThrow(
                () -> new NotFoundException("Опубликованного события с id=" + id + "не найдено")
        );
    }

    public Event getPrivateEventById(long userId, long eventId) {
        User user = userService.getUserById(userId);
        return eventRepo.findByIdAndInitiator(eventId, user).orElseThrow(
                () -> new NotFoundException("Событие с id=" + eventId + " для user=" + userId + " не найдено"));
    }

    public Event updateEventByUser(long userId, long eventId, EventUpdateRequest request) {
        User user = userService.getUserById(userId);
        Event event = this.getById(eventId);

        if (!event.getInitiator().equals(user)) {
            throw new NotFoundException("Событие не найдено или принадлежит другому пользователю");
        }

        if (event.getState().equals(EventState.PUBLISHED)) {
            throw new ConflictException("Нельзя изменять опубликованное событие");
        }

        if (request.getStateAction() != null) {
            StateAction stateAction = request.getStateAction();
            switch (stateAction) {
                case SEND_TO_REVIEW -> event.setState(EventState.PENDING);
                case CANCEL_REVIEW -> event.setState(EventState.CANCELED);
                default ->
                        throw new BadRequestException("Недопустимый stateAction=" + stateAction + " для пользователя");
            }
        }

        return this.updateEvent(event, request);
    }

    public Event updateEventByAdmin(long eventId, EventUpdateRequest request) {
        Event event = this.getById(eventId);

        if (event.getState().equals(EventState.PUBLISHED)) {
            throw new ConflictException("Нельзя изменять опубликованное событие");
        }

        if (request.getStateAction() != null) {
            StateAction stateAction = request.getStateAction();
            switch (stateAction) {
                case PUBLISH_EVENT -> event.setState(EventState.PUBLISHED);
                case REJECT_EVENT -> event.setState(EventState.CANCELED);
                default -> throw new BadRequestException("Недопустимый stateAction=" + stateAction + " для админа");
            }
        }

        return this.updateEvent(event, request);
    }

    private Event getById(long id) {
        return eventRepo.findById(id).orElseThrow(
                () -> new NotFoundException("Не найдено событие id=" + id)
        );
    }

    @Transactional
    private Event updateEvent(Event event, EventUpdateRequest request) {
        if (request.getTitle() != null) {
            event.setTitle(request.getTitle());
        }

        if (request.getAnnotation() != null) {
            event.setAnnotation(request.getAnnotation());
        }

        if (request.getCategory() != null) {
            Category category = categoriesService.getCategoryById(request.getCategory());
            event.setCategory(category);
        }

        if (request.getDescription() != null) {
            event.setDescription(request.getDescription());
        }

        if (request.getEventDate() != null) {
            event.setEventDate(request.getEventDate());
        }

        if (request.getLocation() != null) {
            Location location = eventMapper.toLocation(request.getLocation());
            location = locationRepo.save(location);
            event.setLocation(location);
        }

        if (request.getPaid() != null) {
            event.setPaid(request.getPaid());
        }

        if (request.getParticipantLimit() != null) {
            event.setParticipantLimit(request.getParticipantLimit());
        }

        if (request.getRequestModeration() != null) {
            event.setRequestModeration(request.getRequestModeration());
        }

        return eventRepo.save(event);
    }
}
