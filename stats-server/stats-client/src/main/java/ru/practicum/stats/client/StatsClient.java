package ru.practicum.stats.client;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;
import ru.practicum.stats.dto.HitRequest;
import ru.practicum.stats.dto.StatsResponse;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class StatsClient {
    private final RestClient restClient;
    private final String baseUrl;

    public StatsClient(@Value("${stats-server.url}") String baseUrl) {
        this.baseUrl = baseUrl;
        this.restClient = RestClient.builder()
                .baseUrl(baseUrl).build();
    }


    public List<StatsResponse> stats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {

        String currentUri = UriComponentsBuilder.fromHttpUrl(baseUrl)
                .path("/stats")
                .queryParam("start", start)
                .queryParam("end", end)
                .queryParam("uris", uris)
                .queryParam("unique", unique)
                .toUriString();

        return restClient.get()
                .uri(currentUri)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, ((request, response) -> {
                    throw new StatsClientException(response.getBody().toString());
                }))
                .onStatus(HttpStatusCode::is5xxServerError, ((request, response) -> {
                    throw new StatsClientException(response.getBody().toString());
                }))
                .body(new ParameterizedTypeReference<>() {
                });
    }


    public void hit(HitRequest hitRequest) {
        String currentUri = UriComponentsBuilder.fromHttpUrl(baseUrl).path("/hit").toUriString();

        restClient.post()
                .uri(currentUri)
                .contentType(MediaType.APPLICATION_JSON)
                .body(hitRequest)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, ((request, response) -> {
                    throw new StatsClientException(response.getBody().toString());
                }))
                .onStatus(HttpStatusCode::is5xxServerError, ((request, response) -> {
                    throw new StatsClientException(response.getBody().toString());
                }))
                .toBodilessEntity();
    }
}
