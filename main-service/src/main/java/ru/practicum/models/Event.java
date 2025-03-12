package ru.practicum.models;

import jakarta.persistence.*;
import lombok.*;
import ru.practicum.dto.event.EventState;
import ru.practicum.exceptions.ConflictException;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "events")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "annotation")
    private String annotation;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "initiator_id")
    private User initiator;

    @ManyToOne
    @JoinColumn(name = "location_id")
    private Location location;

    @Column(name = "description")
    private String description;

    @Column(name = "eventDate")
    private LocalDateTime eventDate;

    @Column(name = "paid")
    private Boolean paid;

    @Column(name = "participant_limit")
    private Long participantLimit;

    @Column(name = "request_moderation")
    private Boolean requestModeration;

    @Column(name = "created_on")
    private LocalDateTime createdOn = LocalDateTime.now();

    @Column(name = "published_on")
    private LocalDateTime publishedOn;

    @Column(name = "state")
    @Enumerated(EnumType.STRING)
    private EventState state = EventState.PENDING;

    @Transient
    private Long confirmedRequests;

    @Transient
    private Long views;

    public Event(Event e, long confirmedRequests) {
        this.id = e.id;
        this.title = e.title;
        this.annotation = e.annotation;
        this.category = e.category;
        this.initiator = e.initiator;
        this.description = e.description;
        this.eventDate = e.eventDate;
        this.location = e.location;
        this.paid = e.paid;
        this.participantLimit = e.participantLimit;
        this.requestModeration = e.requestModeration;
        this.state = e.state;
        this.createdOn = e.createdOn;
        this.publishedOn = e.publishedOn;
        this.confirmedRequests = confirmedRequests;
    }

    public void setState(EventState newState) {
        if (newState.equals(EventState.PUBLISHED) && !state.equals(EventState.PENDING)) {
            throw new ConflictException("Нельзя опубликовать события, которые не ожидают публикации");
        }
        if (newState.equals(EventState.CANCELED) && state.equals(EventState.PUBLISHED)) {
            throw new ConflictException("Нельзя отклонить опубликованные события");
        }
        if (newState.equals(EventState.PENDING) && state.equals(EventState.PUBLISHED)) {
            throw new ConflictException("Нельзя изменить статус опубликованного события");
        }
        state = newState;

        if (state.equals(EventState.PUBLISHED)) {
            publishedOn = LocalDateTime.now();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Event event)) return false;
        return Objects.equals(id, event.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }
}
