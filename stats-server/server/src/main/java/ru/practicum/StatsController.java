package ru.practicum;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.stats.dto.HitRequest;
import ru.practicum.stats.dto.StatsResponse;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@RestController
@Validated
@RequestMapping
public class StatsController {
    private final StatsService service;

    @GetMapping("/stats")
    @ResponseStatus(HttpStatus.OK)
    public List<StatsResponse> stats(@RequestParam(name = "start") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,
                                     @RequestParam(name = "end") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end,
                                     @RequestParam(name = "uris", required = false) List<String> uris,
                                     @RequestParam(name = "unique", required = false, defaultValue = "false") boolean unique) {
        return service.stats(start, end, uris, unique);
    }

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public void hit(@RequestBody @Valid HitRequest request) {
        service.hit(request);
    }
}
