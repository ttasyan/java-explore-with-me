package ru.practicum.event.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.EventService;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventShortDto;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/events")
public class PublicEventController {
    private final EventService service;

    @GetMapping
    public List<EventShortDto> getAllPublic(@RequestParam String text, @RequestParam List<Long> categories, @RequestParam boolean paid,
                                            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
                                            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
                                            @RequestParam(defaultValue = "false") boolean onlyAvailable, @RequestParam String sort,
                                            @RequestParam(defaultValue = "0") int from, @RequestParam(defaultValue = "10") int size) {
        return service.getAllPublic(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size);
    }

    @GetMapping("/{id}")
    public EventFullDto getByIdPublic(@PathVariable long id) {
        return service.getByIdPublic(id);
    }
}
