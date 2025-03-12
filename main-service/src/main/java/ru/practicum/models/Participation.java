package ru.practicum.models;

import jakarta.persistence.*;
import lombok.*;
import ru.practicum.dto.ParticipationStatus;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "participations")
@ToString
public class Participation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;

    @ManyToOne
    @JoinColumn(name = "requester_id")
    private User requester;

    @Enumerated(EnumType.STRING)
    private ParticipationStatus status = ParticipationStatus.PENDING;

    @Column(name = "created_on")
    private LocalDateTime created = LocalDateTime.now();
}
