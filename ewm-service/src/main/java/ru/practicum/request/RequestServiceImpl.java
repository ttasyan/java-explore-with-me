package ru.practicum.request;

import jakarta.validation.ConstraintViolationException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.event.EventRepository;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.EventStatus;
import ru.practicum.exception.*;
import ru.practicum.user.User;
import ru.practicum.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class RequestServiceImpl implements RequestService {
    private RequestRepository repository;
    private UserRepository userRepository;
    private EventRepository eventRepository;
    private RequestMapper mapper;

    public List<RequestDto> getById(long userId) {
        findUserById(userId);
        return repository.findByRequesterId(userId).stream()
                .map(request -> mapper.requestToRequestDto(request))
                .toList();
    }

    public RequestDto cancelRequest(long userId, long requestId) {
        findUserById(userId);
        if (repository.findById(requestId).isEmpty()) {
            throw new NotFoundException("Request with id=" + requestId + " not found");
        }

        Request request = repository.findById(requestId).orElseThrow(() ->
                new NotFoundException("Request with id=" + requestId + " not found"));
        request.setStatus(RequestStatusType.CANCELED);
        return mapper.requestToRequestDto(repository.save(request));

    }

    public RequestDto addRequest(long userId, long eventId) {
        try {
            User user = findUserById(userId);
            Event event = eventRepository.findById(eventId).orElseThrow(() ->
                    new NotFoundException("Event with id=" + eventId + " not found"));

            Request request = new Request();
            request.setRequester(user);
            request.setEvent(event);
            request.setCreated(LocalDateTime.now());
            request.setStatus(RequestStatusType.PENDING);

            if (!event.isRequestModeration()) {
                request.setStatus(RequestStatusType.CONFIRMED);
            }

            if (repository.existsByRequesterIdAndEventId(userId, eventId)) {
                throw new DuplicatedDataException("Request already exists");
            }
            if (event.getInitiator().equals(user)) {
                throw new InitiatorException("Initiator can not be requestor");
            }
            if (event.getState().equals(EventStatus.PENDING)) {
                throw new EventNotPublishedException("Event not published");
            }
            if (event.getParticipationLimit() == event.getConfirmedRequests()) {
                throw new RequestLimitException("Participation limit has been reached");
            }
            return mapper.requestToRequestDto(repository.save(request));
        } catch (ConstraintViolationException e) {
            throw new IntegrityConstraintViolationException("Нарушение ограничения");
        }

    }

    private User findUserById(long userId) {
        return userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("User with id=" + userId + " not found"));
    }
}
