package ru.practicum.event.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.event.dto.CommentDto;
import ru.practicum.event.model.Comment;

@Mapper
public interface CommentMapper {
    @Mapping(target = "eventId", source = "event.id")
    CommentDto commentToCommentDto(Comment comment);

}
