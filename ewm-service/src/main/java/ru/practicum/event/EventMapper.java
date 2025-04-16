package ru.practicum.event;

import org.mapstruct.Mapper;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.model.Event;

@Mapper
public interface EventMapper {
    EventFullDto eventToEventFullDto(Event event);
    EventShortDto eventToEventShortDto(Event event);
}
