package ru.practicum.dto;

import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class HitResponseDto {
    private Long id;

    private String app;

    private String uri;

    private String ip;

    private String timestamp;
}
