package ru.practicum.request;

import org.mapstruct.Mapper;

@Mapper
public interface RequestMapper {
    RequestDto requestToRequestDto(Request request);
}
