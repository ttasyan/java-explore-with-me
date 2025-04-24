package ru.practicum.event.service;

import ru.practicum.event.dto.CommentDto;
import ru.practicum.event.dto.CommentRequest;

public interface CommentService {
    CommentDto addComment(long userId, long eventId, CommentRequest request);

    CommentDto patchComment(long userId, long eventId, CommentRequest request, long commentId);

    void deleteById(long userId, long eventId, long commentId);

    void deleteByIdAdmin(long eventId, long commentId);
}
