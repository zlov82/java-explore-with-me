package ru.practicum.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.dto.ParticipationStatus;
import ru.practicum.dto.ParticipationUpdateByEventOwner;
import ru.practicum.dto.ParticipationUpdateByEventOwnerRs;
import ru.practicum.dto.ParticipationUpdateStatus;
import ru.practicum.dto.event.EventState;
import ru.practicum.exceptions.ConflictException;
import ru.practicum.exceptions.NotFoundException;
import ru.practicum.mappers.ParticipationMapper;
import ru.practicum.models.Event;
import ru.practicum.models.Participation;
import ru.practicum.models.User;
import ru.practicum.repository.ParticipationRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ParticipationService {
    private final ParticipationRepository repository;
    private final EventService eventService;
    private final UserService userService;
    private final ParticipationMapper mapper;

    public Participation create(long userId, long eventId) {
        Event event = eventService.getEventById(eventId);
        User user = userService.getUserById(userId);

        if (!event.getState().equals(EventState.PUBLISHED)) {
            throw new ConflictException("нельзя участвовать в неопубликованном событии");
        }
        if (event.getInitiator().getId().equals(user.getId())) {
            throw new ConflictException("инициатор события не может добавить запрос на участие в своём событии");
        }

        if (event.getConfirmedRequests() != 0 && event.getConfirmedRequests().equals(event.getParticipantLimit())) {
            throw new ConflictException("У события достигнут лимит запросов на участие ");
        }

        Participation participation = new Participation();
        participation.setEvent(event);
        participation.setRequester(user);

        if (!event.getRequestModeration() || event.getParticipantLimit() == 0) {
            participation.setStatus(ParticipationStatus.CONFIRMED);
        }

        return repository.save(participation);
    }

    public Participation cancel(long userId, long requestId) {
        Participation participation = repository.findById(requestId).orElseThrow(
                () -> new NotFoundException("Не найден запрос запрос на участие с номером " + requestId)
        );

        User user = userService.getUserById(userId);

        if (!participation.getRequester().equals(user)) {
            throw new ConflictException("нельзя отменить чужую заявку");
        }

        participation.setStatus(ParticipationStatus.CANCELED);
        return repository.save(participation);
    }

    public List<Participation> getUserEventRequest(long userId, long eventId) {
        return repository.findAllRequesterByEventOwner(userId, eventId);
    }

    @Transactional
    public ParticipationUpdateByEventOwnerRs updateParticipationByEventOwner(long userId, long eventId, ParticipationUpdateByEventOwner request) {

        User user = userService.getUserById(userId);
        Event event = eventService.getEventById(eventId);

        if (!event.getInitiator().equals(user)) {
            throw new ConflictException("Невозможно обновлять заявки по эвенту другого пользователя");
        }

        List<Participation> participationList = repository.findAllById(request.getRequestIds());

        if (event.getParticipantLimit() != 0 && event.getRequestModeration()) {

            if (request.getStatus() == ParticipationUpdateStatus.CONFIRMED &&
                    event.getConfirmedRequests() + participationList.size() > event.getParticipantLimit()) {
                throw new ConflictException("Достигнут лимит по заявкам на данное событие");
            }

            for (Participation participation : participationList) {
                if (!participation.getEvent().equals(event)) {
                    throw new ConflictException("Заявка id=" + participation.getId() + "не принадлежит к событию id" +
                            eventId);
                }
            }

            for (Participation participation : participationList) {
                if (!participation.getStatus().equals(ParticipationStatus.PENDING)) {
                    throw new ConflictException("Заявка id" + participation.getId() + " не в статусе PENDING");
                }
            }

            //Меняем статус
            switch (request.getStatus()) {
                case REJECTED -> {
                    participationList.stream().forEach(p -> p.setStatus(ParticipationStatus.REJECTED));
                    repository.saveAll(participationList);
                }

                case CONFIRMED -> {

                    int requestRemains = (int) (event.getParticipantLimit() - event.getConfirmedRequests());
                    int requestToReject = (int) (participationList.size() - requestRemains);


                    if (requestToReject < 0) { // можно подтверждать все
                        participationList.stream().forEach(p -> p.setStatus(ParticipationStatus.CONFIRMED));
                    } else {
                        // проводим [0 до requestRemains)
                        for (int i = 0; i < requestRemains; i++) {
                            participationList.get(i).setStatus(ParticipationStatus.CONFIRMED);
                        }

                        // отбиваем [requestRemains < requestToReject)
                        for (int j = requestRemains; j < requestToReject; j++) {
                            participationList.get(j).setStatus(ParticipationStatus.REJECTED);
                        }
                    }
                    repository.saveAll(participationList);
                }
            }

        }

        List<Participation> confirmedRequests = repository.findAllByEventAndStatus(event, ParticipationStatus.CONFIRMED);
        List<Participation> rejectedRequests = repository.findAllByEventAndStatus(event, ParticipationStatus.REJECTED);

        return new ParticipationUpdateByEventOwnerRs(mapper.toDto(confirmedRequests), mapper.toDto(rejectedRequests));
    }

    public List<Participation> getUserRequests(long userId) {
        User user = userService.getUserById(userId);
        List<Participation> requestList = repository.findAllByRequester(user);

        return requestList.stream()
                .filter(p -> !p.getEvent().getInitiator().equals(user))
                .toList();
    }
}
