package ru.practicum;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.stats.dto.StatsResponse;

import java.time.LocalDateTime;
import java.util.List;

public interface HitRepository extends JpaRepository<EndpointHit, Integer> {
    @Query("SELECT new ru.practicum.stats.dto.StatsResponse(h.app, h.uri, COUNT(h)) FROM EndpointHit h WHERE h.timestamp BETWEEN :start AND :end " +
            "AND (:uris IS NULL OR h.uri IN :uris) GROUP BY h.uri ORDER BY COUNT(h) DESC")
    List<StatsResponse> countHits(@Param("start") LocalDateTime start,
                                  @Param("end") LocalDateTime end,
                                  @Param("uris") List<String> uris);

    @Query("SELECT new ru.practicum.stats.dto.StatsResponse(h.app, h.uri, COUNT(DISTINCT h.ip)) FROM EndpointHit h WHERE h.timestamp BETWEEN :start AND :end " +
            "AND (:uris IS NULL OR h.uri IN :uris) GROUP BY h.uri ORDER BY COUNT(DISTINCT h.ip) DESC")
    List<StatsResponse> countUniqueHits(@Param("start") LocalDateTime start,
                                        @Param("end") LocalDateTime end,
                                        @Param("uris") List<String> uris);
}
