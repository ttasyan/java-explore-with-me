package ru.practicum;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.stats.client.StatsClient;
import ru.practicum.stats.dto.HitRequest;
import ru.practicum.stats.dto.StatsResponse;

import java.util.List;

@RequiredArgsConstructor
@Controller
@Validated
@RequestMapping
public class StatsController {
    private final StatsClient client;

    @GetMapping("/stats")
    public ResponseEntity<List<StatsResponse>> stats(@RequestParam(name = "start") String start,
                                                     @RequestParam(name = "end") String end,
                                                     @RequestParam(name = "uris", required = false) List<String> uris,
                                                     @RequestParam(name = "unique", required = false, defaultValue = "false") boolean unique) {
        return client.stats("/stats", start, end, uris, unique);
    }

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public void hit(@RequestBody @Valid HitRequest request) {
        client.hit("/hit", request);
    }
}
