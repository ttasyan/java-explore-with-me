package ru.practicum;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.stats.dto.HitRequest;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Component
public class EndpointHitMapper {
    public EndpointHit fromHit(HitRequest request) {
        EndpointHit hit = new EndpointHit();
        hit.setIp(request.getIp());
        hit.setApp(request.getApp());
        hit.setUri(request.getUri());
        hit.setTimestamp(request.getTimestamp());
        return hit;
    }
}
