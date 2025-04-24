package ru.practicum.event.service;



import jakarta.servlet.http.HttpServletRequest;
import ru.practicum.event.dto.*;
import ru.practicum.request.RequestDto;

import java.time.LocalDateTime;
import java.util.List;

public interface EventService {
    List<EventFullDto> getAllAdmin(List<Long> users, List<String> states, List<Long> categories, LocalDateTime rangeStart,
                                   LocalDateTime rangeEnd, int from, int size);

    EventFullDto modifyEventAdmin(long eventId, UpdateEventAdminRequest newEvent);

    List<EventFullDto> getAllByCurrentUser(long userId, int from, int size);

    EventFullDto addEvent(long userId, NewEventDto newEvent);

    EventFullDto getEventById(long userId, long eventId);

    EventFullDto modifyEventByUser(long userId, long eventId, UpdateEventUserRequest newEvent);

    List<RequestDto> getRequestsById(long userId, long eventId);

    EventRequestStatusUpdateResult modifyRequestStatus(long userId, long eventId, EventRequestStatusUpdateRequest newRequest);

    List<EventShortDto> getAllPublic(String text, List<Long> categories, boolean paid, LocalDateTime rangeStart,
                                     LocalDateTime rangeEnd, boolean onlyAvailable, String sort, int from, int size,
                                     HttpServletRequest httpServletRequest);

    EventFullDto getByIdPublic(long id, HttpServletRequest httpServletRequest);
}
