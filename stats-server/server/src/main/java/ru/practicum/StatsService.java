package ru.practicum;

import ru.practicum.stats.dto.HitRequest;
import ru.practicum.stats.dto.StatsResponse;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsService {
    List<StatsResponse> stats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique);

    void hit(HitRequest request);
}
