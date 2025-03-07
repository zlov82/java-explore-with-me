package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.dto.ParticipationStatus;
import ru.practicum.models.Event;
import ru.practicum.models.Participation;

import java.util.List;

public interface ParticipationRepository extends JpaRepository<Participation, Long> {

    @Query("""
            SELECT pr
            FROM Participation as pr
            LEFT JOIN Event e ON e = pr.event
            WHERE e.initiator.id = :userId
            AND pr.event.id = :eventId
            """)
    List<Participation> findAllRequesterByEventOwner(long userId, long eventId);


    List<Participation> findAllByEventAndStatus(Event event, ParticipationStatus participationStatus);
}
