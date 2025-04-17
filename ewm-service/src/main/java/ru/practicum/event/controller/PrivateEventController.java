package ru.practicum.event.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.EventService;
import ru.practicum.event.dto.*;
import ru.practicum.request.RequestDto;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users/{userId}/events")
public class PrivateEventController {
    private final EventService service;

    @GetMapping
    public List<EventFullDto> getAllByCurrentUser(@PathVariable long userId, @RequestParam(defaultValue = "0") int from,
                                                  @RequestParam(defaultValue = "10") int size) {
        return service.getAllByCurrentUser(userId, from, size);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto addEvent(@PathVariable long userId, @RequestBody @Valid NewEventDto newEvent) {
        return service.addEvent(userId, newEvent);
    }

    @GetMapping("/{eventId}")
    public EventFullDto getEventById(@PathVariable long userId, @PathVariable long eventId) {
        return service.getEventById(userId, eventId);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto modifyEventByUser(@PathVariable long userId, @PathVariable long eventId,
                                          @RequestBody @Valid UpdateEventUserRequest newEvent) {
        return service.modifyEventByUser(userId, eventId, newEvent);
    }

    @GetMapping("/{eventId}/requests")
    public List<RequestDto> getRequestsById(@PathVariable long userId, @PathVariable long eventId) {
        return service.getRequestsById(userId, eventId);
    }

    @PatchMapping("/{eventId}/requests")
    public EventRequestStatusUpdateResult modifyRequestStatus(@PathVariable long userId, @PathVariable long eventId,
                                                              @RequestBody @Valid EventRequestStatusUpdateRequest newRequest) {
        return service.modifyRequestStatus(userId, eventId, newRequest);
    }
}
