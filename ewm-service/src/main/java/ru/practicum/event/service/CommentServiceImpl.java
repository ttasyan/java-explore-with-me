package ru.practicum.event.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.event.CommentRepository;
import ru.practicum.event.EventRepository;
import ru.practicum.event.dto.CommentDto;
import ru.practicum.event.dto.CommentRequest;
import ru.practicum.event.mapper.CommentMapper;
import ru.practicum.event.model.Comment;
import ru.practicum.event.model.Event;
import ru.practicum.exception.NotFoundException;
import ru.practicum.exception.ValidationException;
import ru.practicum.user.User;
import ru.practicum.user.UserRepository;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class CommentServiceImpl implements CommentService {
    private CommentRepository repository;
    private UserRepository userRepository;
    private EventRepository eventRepository;
    private CommentMapper mapper;

    public CommentDto addComment(long userId, long eventId, CommentRequest request) {
        Comment comment = new Comment();
        comment.setAuthor(findByIdUser(userId));
        comment.setEvent(findByIdEvent(eventId));
        comment.setPublishedOn(LocalDateTime.now());
        comment.setText(request.getText());
        return mapper.commentToCommentDto(repository.save(comment));
    }

    public CommentDto patchComment(long userId, long eventId, CommentRequest request, long commentId) {
        findByIdEvent(eventId);
        findByIdUser(userId);
        Comment comment = repository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Comment with id=" + commentId + " not found"));
        if (userId != comment.getAuthor().getId()) {
            throw new ValidationException("Only author can modify their comment");
        }
        if (request.getText() != null && !request.getText().isBlank()) {
            comment.setText(request.getText());
        }
        return mapper.commentToCommentDto(repository.save(comment));
    }

    public void deleteById(long userId, long eventId, long commentId) {
        findByIdEvent(eventId);
        findByIdUser(userId);
        Comment comment = findByIdComment(commentId);
        if (userId != comment.getAuthor().getId()) {
            throw new ValidationException("Only author can delete their comment");
        }
        repository.deleteById(commentId);
    }

    public void deleteByIdAdmin(long eventId, long commentId) {
        findByIdEvent(eventId);
        findByIdComment(commentId);
        repository.deleteById(commentId);
    }


    private User findByIdUser(long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id=" + userId + " not found"));
    }

    private Event findByIdEvent(long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event with id=" + eventId + " not found"));
    }

    private Comment findByIdComment(long commentId) {
        return repository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Comment with id=" + commentId + " not found"));
    }
}
