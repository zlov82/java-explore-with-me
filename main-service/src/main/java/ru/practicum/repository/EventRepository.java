package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.dto.EventState;
import ru.practicum.models.Event;
import ru.practicum.models.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long> {

    @Query("""
            SELECT ev
            FROM Event ev
            WHERE ev.initiator = :user
            ORDER BY id
            LIMIT :size
            OFFSET :from
            """)
    List<Event> selectEvents(User user, Integer from, Integer size);

    @Query("""
            SELECT e
            FROM Event e
            WHERE (:userIds IS NULL OR e.initiator.id IN :userIds)
            AND (:categoriesIds IS NULL OR e.category.id IN :categoriesIds)
            AND (:states IS NULL OR e.state IN :states)
            AND (CAST(:start AS timestamp) IS NULL OR e.eventDate >= :start)
            AND (CAST(:end AS timestamp) IS NULL OR e.eventDate <= :end)
            ORDER BY e.eventDate DESC
            LIMIT :size
            OFFSET :from
            """)
    List<Event> findAllByParams(List<Long> userIds, List<Integer> categoriesIds, List<String> states,
                                LocalDateTime start, LocalDateTime end, Integer size, Integer from);

    @Query("""
            SELECT e
            FROM Event e
            WHERE (:text IS NULL OR LOWER(e.annotation) LIKE %:text% OR LOWER(e.description) LIKE %:text%)
            AND (:categories IS NULL OR e.category.id IN :categories)
            AND (:paid IS NULL OR e.paid = :paid)
            AND (CAST(:start AS timestamp) IS NULL OR e.eventDate >= :start)
            AND (CAST(:end AS timestamp) IS NULL OR e.eventDate <= :end)
            AND e.state = 'PUBLISHED'
            ORDER BY e.eventDate ASC
            """)
    List<Event> findTextInEventsWithParams(String text, List<Integer> categories, Boolean paid,
                                           LocalDateTime start, LocalDateTime end);


    Optional<Event> findByIdAndInitiator(long eventId, User user);

    Optional<Event> findByIdAndState(long id, EventState eventState);
}
