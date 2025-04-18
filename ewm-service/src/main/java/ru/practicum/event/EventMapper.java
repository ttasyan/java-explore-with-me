package ru.practicum.event;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.model.Event;

@Mapper
public interface EventMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdOn", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "state", expression = "java(ru.practicum.event.model.EventStatus.PENDING)")
    @Mapping(target = "participationLimit", source = "participationLimit", defaultValue = "0")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    EventFullDto eventToEventFullDto(Event event);

    @Mapping(target = "id", ignore = true)
    //@Mapping(target = "category.id", source = "category")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    EventShortDto eventToEventShortDto(Event event);
}
