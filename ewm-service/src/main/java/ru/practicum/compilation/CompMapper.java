package ru.practicum.compilation;

import org.mapstruct.Mapper;

@Mapper
public interface CompMapper {
    CompilationDto complicationToComplicationDto(Compilation compilation);
}
