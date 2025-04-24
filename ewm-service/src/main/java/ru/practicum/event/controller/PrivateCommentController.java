package ru.practicum.event.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.service.CommentService;
import ru.practicum.event.dto.CommentDto;
import ru.practicum.event.dto.CommentRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users/{userId}/events/{eventId}/comment")
public class PrivateCommentController {
    private final CommentService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDto addComment(@PathVariable long userId, @PathVariable long eventId,
                                 @RequestBody @Valid CommentRequest request) {
        return service.addComment(userId, eventId, request);
    }

    @PatchMapping("/{commentId}")
    public CommentDto patchComment(@PathVariable long userId, @PathVariable long eventId,
                                   @RequestBody @Valid CommentRequest request, @PathVariable long commentId) {
        return service.patchComment(userId, eventId, request, commentId);
    }

    @DeleteMapping("/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable long userId, @PathVariable long eventId, @PathVariable long commentId) {
        service.deleteById(userId, eventId, commentId);
    }
}
