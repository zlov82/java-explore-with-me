package ru.practicum.services;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.StatMapper;
import ru.practicum.dto.EndpointHitRequestDto;
import ru.practicum.dto.ViewStatsResponseDto;
import ru.practicum.models.Hit;
import ru.practicum.models.StatShortResponse;
import ru.practicum.repository.StatRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StatService {
    private final StatRepository repository;

    public Hit saveHit(@Valid EndpointHitRequestDto requestHitDto) {
        Hit newHit = StatMapper.toStatistic(requestHitDto);
        return repository.save(newHit);
    }

    public List<ViewStatsResponseDto> getStatistics(LocalDateTime start, LocalDateTime end,
                                                    List<String> uris, Boolean unique) {
        List<StatShortResponse> shortResponses;
        if (unique) {
            if (uris != null) {
                shortResponses = repository.selectUniquesStat(uris, start, end);
            } else {
                shortResponses = repository.selectUniquesStat(start, end);
            }
        } else {
            if (uris != null) {
                shortResponses = repository.selectNonUniquesStat(uris, start, end);
            } else {
                shortResponses = repository.selectNonUniquesStat(start, end);
            }
        }

        return shortResponses.stream()
                .map(StatMapper::toStatsResponseDto)
                .collect(Collectors.toList());
    }
}
