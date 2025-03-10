package ru.practicum.dto;

import lombok.Data;

import java.util.List;

@Data
public class ParticipationUpdateByEventOwner {
    private List<Long> requestIds;
    private ParticipationUpdateStatus status;
}
