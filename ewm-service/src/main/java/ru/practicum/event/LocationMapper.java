package ru.practicum.event;

import org.mapstruct.Mapper;
import ru.practicum.event.dto.LocationDto;
import ru.practicum.event.model.Location;

@Mapper
public interface LocationMapper {
    Location locationDtoToLocation(LocationDto locationDto);
}
