package ru.practicum.event.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.EventService;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.UpdateEventAdminRequest;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/events")
public class AdminEventController {
    private final EventService service;

    @GetMapping
    public List<EventFullDto> getAll(@RequestParam List<Long> users, @RequestParam List<String> states,
                                     @RequestParam List<Long> categories,
                                     @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
                                     @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
                                     @RequestParam(defaultValue = "0") int from, @RequestParam(defaultValue = "10") int size) {
        return service.getAllAdmin(users, states, categories, rangeStart, rangeEnd, from, size);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto modifyEvent(@PathVariable long eventId, @RequestBody @Valid UpdateEventAdminRequest newEvent) {
        return service.modifyEventAdmin(eventId, newEvent);
    }
}
