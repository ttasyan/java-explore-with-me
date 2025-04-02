package ru.practicum.stats.client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.practicum.stats.dto.HitRequest;
import ru.practicum.stats.dto.StatsResponse;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class StatsClient {
    private final RestTemplate rest;
    private final HttpHeaders headers;
    private final HitRepository hitRepository;

    @Autowired
    public StatsClient(RestTemplate rest, HitRepository repository) {
        this.hitRepository = repository;
        this.rest = rest;
        this.headers = new HttpHeaders();
        this.headers.setContentType(MediaType.APPLICATION_JSON);
        this.headers.setAccept(List.of(MediaType.APPLICATION_JSON));
    }

    public ResponseEntity<List<StatsResponse>> stats(String path, String start, String end, List<String> uris, boolean unique) {

        LocalDateTime startDateTime = LocalDateTime.parse(start);
        LocalDateTime endDateTime = LocalDateTime.parse(end);
        List<StatsResponse> stats;
        if (unique) {
            stats = hitRepository.countUniqueHits(startDateTime, endDateTime, uris);
        } else {
            stats = hitRepository.countHits(startDateTime, endDateTime, uris);
        }
        HttpEntity<List<StatsResponse>> requestEntity = new HttpEntity<>(stats, headers);
        return rest.exchange(path, HttpMethod.GET, requestEntity, new ParameterizedTypeReference<List<StatsResponse>>() {
        });
    }

    public void hit(String path, HitRequest request) {
        HttpEntity<HitRequest> requestEntity = new HttpEntity<>(request, headers);
        rest.exchange(path, HttpMethod.POST, requestEntity, HitRequest.class);
    }
}
