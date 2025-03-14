package ru.practicum.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.HitRequestDto;
import ru.practicum.dto.ViewStatsResponseDto;
import ru.practicum.exception.BadRequestException;
import ru.practicum.models.Hit;
import ru.practicum.services.StatService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@RestController
@Tag(name = "StatController")
@RequiredArgsConstructor
public class StatController {

    private final StatService statService;


    @Operation(summary = "Получение статистики по посещениям")
    @GetMapping("/stats")
    public List<ViewStatsResponseDto> getStats(
            @Parameter(description = "Начало периода в формате yyyy-MM-dd HH:mm:ss")
            @RequestParam String start,

            @Parameter(description = "Конец периода в формате yyyy-MM-dd HH:mm:ss")
            @RequestParam String end,

            @RequestParam(required = false)
            @Parameter(description = "Список uri для которых нужно выгрузить статистику") List<String> uris,

            @Parameter(description = "Считать только уникальные IP адреса")
            @RequestParam(required = false, defaultValue = "false")
            Boolean unique) {
        String dateTimeFormat = "yyyy-MM-dd HH:mm:ss";
        LocalDateTime dateStart = LocalDateTime.parse(start, DateTimeFormatter.ofPattern(dateTimeFormat));
        LocalDateTime dateEnd = LocalDateTime.parse(end, DateTimeFormatter.ofPattern(dateTimeFormat));
        if (dateStart.isAfter(dateEnd)) {
            throw new BadRequestException("dateStart позже dateEnd");
        }
        log.info("Request Get '/stat' dateStart = {}, dateEnd = {}, uris = {}, unique = {}", dateStart, dateEnd, uris, unique);
        List<ViewStatsResponseDto> rs = statService.getStatistics(dateStart, dateEnd, uris, unique);
        log.info("Response Get /stat: {}", rs);
        return rs;
    }

    @Operation(summary = "Сохранение информации о том, что к эндпоинту был запрос")
    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public void postHit(@RequestBody @Valid HitRequestDto requestHitDto) {
        log.info("Request Post '/hit' body: {} ", requestHitDto);
        Hit resp = statService.saveHit(requestHitDto);
    }

}