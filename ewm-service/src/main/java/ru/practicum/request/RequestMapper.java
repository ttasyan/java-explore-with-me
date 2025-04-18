package ru.practicum.request;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface RequestMapper {
    @Mapping(target = "event", source = "event.id")
    @Mapping(target = "requester", source = "requester.id")
    RequestDto requestToRequestDto(Request request);
}
