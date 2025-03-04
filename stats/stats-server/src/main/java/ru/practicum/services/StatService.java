package ru.practicum.services;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.StatMapper;
import ru.practicum.dto.HitRequestDto;
import ru.practicum.dto.ViewStatsResponseDto;
import ru.practicum.models.Hit;
import ru.practicum.models.StatShortResponse;
import ru.practicum.repository.StatRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class StatService {
    private final StatRepository repository;
    private final StatMapper statMapper;

    public Hit saveHit(@Valid HitRequestDto requestHitDto) {
        Hit newHit = statMapper.toStatistic(requestHitDto);
        log.info("Service saveHit() hit: {}", newHit);
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

        log.info("Service return getStatistics: {}", shortResponses);

        return shortResponses.stream()
                .map(statMapper::toStatsResponseDto)
                .collect(Collectors.toList());
    }
}
