package ru.practicum.ewm.client;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ru.practicum.dto.HitRequestDto;
import ru.practicum.dto.HitResponseDto;
import ru.practicum.dto.ViewStatsResponseDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.StringJoiner;

@Component
public class StatsClient {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final RestTemplate restTemplate;

    public StatsClient(String urlForRest) {
        restTemplate = new RestTemplateBuilder()
                .rootUri(urlForRest)
                .build();
    }

    public HitResponseDto hit(String app, String uri, String ip, LocalDateTime timestamp) {

        HitRequestDto request = HitRequestDto.builder()
                .app(app)
                .uri(uri)
                .ip(ip)
                .timestamp(timestamp)
                .build();

        return restTemplate.postForObject("/hit", request, HitResponseDto.class);
    }

    public List<ViewStatsResponseDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {

        StringJoiner queryString = new StringJoiner("&");
        queryString.add("start=" + formatter.format(start));
        queryString.add("end=" + formatter.format(end));

        if (uris != null && !uris.isEmpty()) {
            queryString.add("uris=" + String.join("uris=", uris));
        }

        queryString.add("unique=" + unique);

        ResponseEntity<List<ViewStatsResponseDto>> response = restTemplate
                .exchange("/stats?" + queryString, HttpMethod.GET, null, new ParameterizedTypeReference<List<ViewStatsResponseDto>>() {
                });

        return response.getBody();
    }


}
