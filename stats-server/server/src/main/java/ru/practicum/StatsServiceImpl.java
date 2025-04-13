package ru.practicum;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.stats.dto.HitRequest;
import ru.practicum.stats.dto.StatsResponse;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor(onConstructor_ = @Autowired)
public class StatsServiceImpl implements StatsService {
    private final HitRepository repository;
    private final EndpointHitMapper mapper;

    @Override
    public List<StatsResponse> stats(LocalDateTime start, LocalDateTime end,
                                     List<String> uris, boolean unique) {
        List<StatsResponse> statsResponseList;
        if (unique) {
            statsResponseList = repository.countUniqueHits(start, end, uris);
        } else {
            statsResponseList = repository.countHits(start, end, uris);
        }
        return statsResponseList;
    }

    @Override
    public void hit(HitRequest request) {
        EndpointHit hit = mapper.fromHit(request);
        repository.save(hit);
    }
}
