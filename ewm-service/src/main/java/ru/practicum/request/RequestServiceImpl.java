package ru.practicum.request;

import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
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

        Request request = repository.findById(requestId).orElseThrow(() ->
                new NotFoundException("Request with id=" + requestId + " not found"));
        if (!request.getRequester().getId().equals(userId)) {
            throw new InitiatorException("You can cancel only your own requests");
        }
        request.setStatus(RequestStatusType.CANCELED);
        return mapper.requestToRequestDto(repository.save(request));

    }

    public RequestDto addRequest(long userId, long eventId) {
        try {
            User user = findUserById(userId);
            Event event = eventRepository.findById(eventId).orElseThrow(() ->
                    new NotFoundException("Event with id=" + eventId + " not found"));


            if (repository.existsByRequesterIdAndEventId(userId, eventId)) {
                throw new DuplicatedDataException("Request already exists");
            }
            validate(event, user);

            if (event.getParticipantLimit() != 0 && event.getParticipantLimit() <= event.getConfirmedRequests()) {
                throw new RequestLimitException("Participation limit has been reached");
            }
            Request request = new Request();
            request.setRequester(user);
            request.setEvent(event);
            request.setCreated(LocalDateTime.now());
            request.setStatus(event.isRequestModeration()
                    ? RequestStatusType.PENDING
                    : RequestStatusType.CONFIRMED);
            if (event.getParticipantLimit() == 0 || !event.isRequestModeration()) {
                request.setStatus(RequestStatusType.CONFIRMED);
                event.setConfirmedRequests(event.getConfirmedRequests() + 1);
            }
            return mapper.requestToRequestDto(repository.save(request));
        } catch (DataIntegrityViolationException e) {
            throw new IntegrityConstraintViolationException("Нарушение ограничения");
        }

    }

    private User findUserById(long userId) {
        return userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("User with id=" + userId + " not found"));
    }

    private void validate(Event event, User user) {
        if (event.getInitiator().equals(user)) {
            throw new InitiatorException("Initiator can not be requestor");
        }
        if (event.getState().equals(EventStatus.PENDING)) {
            throw new EventNotPublishedException("Event not published");
        }
    }
}
