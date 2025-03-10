package ru.practicum.services;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.dto.ViewStatsResponseDto;
import ru.practicum.dto.event.*;
import ru.practicum.ewm.client.StatsClient;
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
import java.time.Month;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventService {
    private static final LocalDateTime VIEWS_FROM = LocalDateTime.of(1970, Month.JANUARY, 1, 0, 0, 0);
    private final UserService userService;
    private final CategoriesService categoriesService;
    private final EventRepository eventRepo;
    private final LocationRepository locationRepo;
    private final EventMapper eventMapper;
    private final StatsClient statsClient;

    @Transactional
    public Event createEvent(@Valid EventCreateRequest request, long userId) {

        log.info("create event {} by user {}", request, userId);
        Event event = eventMapper.toEvent(request);
        if (event.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
            throw new BadRequestException("Событие не может быть раньше, чем через два часа от текущего момента");
        }

        User author = userService.getUserById(userId);
        Category category = categoriesService.getCategoryById(request.getCategory());

        event.setInitiator(author);
        event.setCategory(category);

        locationRepo.save(event.getLocation());

        return eventRepo.save(event);
    }

    public List<Event> getPrivateEventsByUser(long userId, int from, int size) {
        User user = userService.getUserById(userId);
        return eventRepo.selectEventsByUserWithSizeAndOffset(user, from, size);
    }


    public List<Event> getPublicEvents(String text, List<Integer> categories, Boolean paid, LocalDateTime rangeStart,
                                       LocalDateTime rangeEnd, Boolean onlyAvailable, EventSearch sort, Integer from, Integer size) {

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

        this.getEventsViews(nonSortedEvents);

        Comparator<Event> comp = switch (sort) {
            case EVENT_DATE -> Comparator.comparing(Event::getEventDate);
            case VIEWS -> Comparator.comparing(Event::getViews);
        };

        return nonSortedEvents.stream()
                .sorted(comp)
                .skip(from)
                .limit(size)
                .toList();

    }

    public List<Event> getEventsByAdmin(List<Long> userIds, List<String> states, List<Integer> categoryIds,
                                        LocalDateTime rangeStart, LocalDateTime rangeEnd, int from, int size) {

        List<Event> events = eventRepo.findAllByParams(userIds, categoryIds, states, rangeStart, rangeEnd, size, from);
        return this.getEventsViews(events);
    }


    public Event getPublicEventById(long id) {
        Event event = eventRepo.findByIdAndState(id, EventState.PUBLISHED).orElseThrow(
                () -> new NotFoundException("Опубликованного события с id=" + id + "не найдено")
        );
        return this.getEventsViews(List.of(event)).getFirst();

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

    public Event getEventById(long id) {
        return this.getById(id);
    }

    private Event getById(long id) {
        return eventRepo.findFullEventById(id).orElseThrow(
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

    private List<Event> getEventsViews(List<Event> events) {

        List<String> eventsIds = events.stream()
                .map(event -> "/events/" + event.getId())
                .collect(Collectors.toList());

        Map<String, Long> mapOfViews = statsClient.getStats(VIEWS_FROM, LocalDateTime.now(), eventsIds, true)
                .stream()
                .collect(Collectors.toMap(ViewStatsResponseDto::getUri, ViewStatsResponseDto::getHits));

        events.forEach(event -> event.setViews(mapOfViews.getOrDefault("/events/" + event.getId(), 0L)));

        return events;
    }


    public List<Event> getEventsByIds(Set<Long> events) {
        return this.getEventsViews(eventRepo.findFullEventByIds(events));
    }
}
