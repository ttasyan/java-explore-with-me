package ru.practicum.stats.client;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.stats.dto.HitRequest;
import ru.practicum.stats.dto.StatsResponse;

import java.time.LocalDateTime;
import java.util.List;

public interface HitRepository extends JpaRepository<HitRequest, Integer> {
    @Query("SELECT h.uri, COUNT(h) FROM Hit h WHERE h.timestamp BETWEEN :start AND :end " +
            "AND (:uris IS NULL OR h.uri IN :uris) GROUP BY h.uri")
    List<StatsResponse> countHits(@Param("start") LocalDateTime start,
                                  @Param("end") LocalDateTime end,
                                  @Param("uris") List<String> uris);

    @Query("SELECT h.uri, COUNT(DISTINCT h.ip) FROM Hit h WHERE h.timestamp BETWEEN :start AND :end " +
            "AND (:uris IS NULL OR h.uri IN :uris) GROUP BY h.uri")
    List<StatsResponse> countUniqueHits(@Param("start") LocalDateTime start,
                                        @Param("end") LocalDateTime end,
                                        @Param("uris") List<String> uris);
}
