package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.dto.event.EventState;
import ru.practicum.models.Event;
import ru.practicum.models.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface EventRepository extends JpaRepository<Event, Long> {

    @Query("""
            SELECT new Event(ev, COUNT(p.id))
            FROM Event ev
            LEFT JOIN Participation p ON p.event = ev AND p.status = 'CONFIRMED'
            WHERE ev.initiator = :user
            GROUP BY ev
            ORDER BY ev.id
            LIMIT :size
            OFFSET :from
            """)
    List<Event> selectEventsByUserWithSizeAndOffset(User user, Integer from, Integer size);

    @Query("""
            SELECT new Event(e, COUNT(p.id))
            FROM Event e
            LEFT JOIN Participation p ON p.event = e AND p.status = 'CONFIRMED'
            WHERE (:userIds IS NULL OR e.initiator.id IN :userIds)
            AND (:categoriesIds IS NULL OR e.category.id IN :categoriesIds)
            AND (:states IS NULL OR e.state IN :states)
            AND (CAST(:start AS timestamp) IS NULL OR e.eventDate >= :start)
            AND (CAST(:end AS timestamp) IS NULL OR e.eventDate <= :end)
            GROUP BY e
            ORDER BY e.eventDate DESC
            LIMIT :size
            OFFSET :from
            """)
    List<Event> findAllByParams(List<Long> userIds, List<Integer> categoriesIds, List<String> states,
                                LocalDateTime start, LocalDateTime end, Integer size, Integer from);

    @Query("""
            SELECT new Event(e, COUNT(p.id))
            FROM Event e
            LEFT JOIN Participation p ON p.event = e AND p.status = 'CONFIRMED'
            WHERE 1=1
            AND (:categoriesIds IS NULL OR e.category.id IN :categoriesIds)
            AND (:states IS NULL OR e.state IN :states)
            AND p.requester.id in :memberIds
            AND (CAST(:start AS timestamp) IS NULL OR e.eventDate >= :start)
            AND (CAST(:end AS timestamp) IS NULL OR e.eventDate <= :end)
            GROUP BY e
            ORDER BY e.eventDate DESC
            LIMIT :size
            OFFSET :from
            """)
    List<Event> findAllByParamsAndMemberIds(List<Long> memberIds, List<Integer> categoriesIds, List<String> states,
                                            LocalDateTime start, LocalDateTime end, Integer size, Integer from);

    @Query("""
            SELECT new Event(e, COUNT(p.id))
            FROM Event e
            LEFT JOIN Participation p ON p.event = e AND p.status = 'CONFIRMED'
            WHERE (:text IS NULL OR LOWER(e.annotation) LIKE %:text% OR LOWER(e.description) LIKE %:text%)
            AND (:categories IS NULL OR e.category.id IN :categories)
            AND (:paid IS NULL OR e.paid = :paid)
            AND (CAST(:start AS timestamp) IS NULL OR e.eventDate >= :start)
            AND (CAST(:end AS timestamp) IS NULL OR e.eventDate <= :end)
            AND e.state = 'PUBLISHED'
            GROUP BY e
            ORDER BY e.eventDate ASC
            """)
    List<Event> findTextInEventsWithParams(String text, List<Integer> categories, Boolean paid,
                                           LocalDateTime start, LocalDateTime end);


    @Query("""
            SELECT new Event(e, COUNT(p.id))
            FROM Event e
            LEFT JOIN Participation p ON p.event = e AND p.status = 'CONFIRMED'
            WHERE e.id = :eventId
            AND e.initiator = :user
            GROUP BY e
            ORDER BY e.eventDate ASC
            """)
    Optional<Event> findByIdAndInitiator(long eventId, User user);


    @Query("""
            SELECT new Event(e, COUNT(p.id))
            FROM Event e
            LEFT JOIN Participation p ON p.event = e AND p.status = 'CONFIRMED'
            WHERE e.id = :eventId
            AND e.state = :eventState
            GROUP BY e
            ORDER BY e.eventDate ASC
            """)
    Optional<Event> findByIdAndState(long eventId, EventState eventState);

    @Query("""
            SELECT new Event(e, COUNT(p.id))
            FROM Event e
            LEFT JOIN Participation p ON p.event = e AND p.status = 'CONFIRMED'
            WHERE e.id = :id
            GROUP BY e
            """)
    Optional<Event> findFullEventById(long id);

    @Query("""
            SELECT new Event(e, COUNT(p.id))
            FROM Event e
            LEFT JOIN Participation p ON p.event = e AND p.status = 'CONFIRMED'
            WHERE e.id IN :ids
            GROUP BY e
            """)
    List<Event> findFullEventByIds(Set<Long> ids);

}
