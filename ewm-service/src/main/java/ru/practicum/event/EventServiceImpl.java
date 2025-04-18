package ru.practicum.event;

import jakarta.validation.ConstraintViolationException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.category.CategoryRepository;
import ru.practicum.event.dto.*;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.EventStateAction;
import ru.practicum.event.model.EventStatus;
import ru.practicum.event.model.Location;
import ru.practicum.exception.*;
import ru.practicum.request.*;
import ru.practicum.user.User;
import ru.practicum.user.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class EventServiceImpl implements EventService {
    private EventMapper mapper;
    private EventRepository repository;
    private LocationRepository locationRepository;
    private UserRepository userRepository;
    private RequestRepository requestRepository;
    private RequestMapper requestMapper;
    private CategoryRepository categoryRepository;

    public List<EventFullDto> getAllAdmin(List<Long> users, List<String> states, List<Long> categories, LocalDateTime rangeStart,
                                          LocalDateTime rangeEnd, int from, int size) {
        int page = from / size;
        Pageable pageable = PageRequest.of(page, size);
        Page<Event> events = repository.findWithParams(users, states, categories, rangeStart, rangeEnd, pageable);
        if (users == null && states == null && categories == null && rangeStart == null && rangeEnd == null) {
            return repository.findAll(pageable).stream().map(event -> mapper.eventToEventFullDto(event)).toList();
        }
        return events.stream().map(event -> mapper.eventToEventFullDto(event)).toList();
    }

    public EventFullDto modifyEventAdmin(long eventId, UpdateEventAdminRequest newEvent) {

        Event event = findById(eventId);

        if (event.getEventDate().isAfter(event.getPublishedOn().plusHours(1)) &&
                event.getState().equals(EventStatus.PUBLISHED)) {
            throw new PublicationException("For the requested operation the conditions are not met");
        }

        EventStateAction newStateAction = newEvent.getStateAction();
        if (!event.getState().equals(EventStatus.PENDING) &&
                newStateAction.equals(EventStateAction.PUBLISH_EVENT)) {
            throw new PublicationException("Event is not in pending stage");
        }

        if (newStateAction.equals(EventStateAction.REJECT_EVENT) && event.getState().equals(EventStatus.PUBLISHED)) {
            throw new PublicationException("Can not reject published event");
        }
        mapStates(newStateAction, event);

        if (newEvent.getLocation() != null) {
            Location location = locationRepository.findByLatAndLon(newEvent.getLocation().getLat(),
                    newEvent.getLocation().getLon());
            event.setLocation(location);
        }
        if (event.getState().equals(EventStatus.PUBLISHED)) {
            event.setPublishedOn(LocalDateTime.now());
        }

        return mapper.eventToEventFullDto(repository.save(event));


    }

    public List<EventFullDto> getAllByCurrentUser(long userId, int from, int size) {
        findByIdUser(userId);
        int page = from / size;
        Pageable pageable = PageRequest.of(page, size);
        Page<Event> events = repository.findAll(pageable);

        return events.stream().map(event -> mapper.eventToEventFullDto(event)).toList();
    }

    public EventFullDto addEvent(long userId, NewEventDto newEvent) {
        try {
            User user = findByIdUser(userId);
            if (LocalDateTime.now().plusHours(2).isAfter(newEvent.getEventDate())) {
                throw new PublicationException("Date can not be less than 2 hours before now");
            }
            Event event = Event.builder()
                    .title(newEvent.getTitle())
                    .eventDate(newEvent.getEventDate())
                    .description(newEvent.getDescription())
                    .createdOn(LocalDateTime.now())
                    .initiator(user)
                    .state(EventStatus.PENDING)
                    .confirmedRequests(0)
                    .participationLimit(newEvent.getParticipationLimit())
                    .requestModeration(newEvent.getRequestModeration())
                    .paid(newEvent.getPaid())
                    .location(locationRepository.findByLatAndLon(newEvent.getLocation().getLat(), newEvent.getLocation().getLon()))
                    .annotation(newEvent.getAnnotation())
                    .category(categoryRepository.findById(newEvent.getCategory())
                            .orElseThrow(() -> new NotFoundException("Category not found")))
                    .build();
            return mapper.eventToEventFullDto(repository.save(event));
        } catch (ConstraintViolationException e) {
            throw new IntegrityConstraintViolationException("Нарушение ограничения");
        }
    }

    public EventFullDto getEventById(long userId, long eventId) {
        findByIdUser(userId);
        return mapper.eventToEventFullDto(repository.findByIdAndInitiatorId(eventId, userId));
    }

    public EventFullDto modifyEventByUser(long userId, long eventId, UpdateEventUserRequest newEvent) {
        try {
            findByIdUser(userId);
            Event event = findById(eventId);
            if (event.getState().equals(EventStatus.PUBLISHED)) {
                throw new EventAlreadyPublished("Can not change published event");
            }
            if (LocalDateTime.now().plusHours(2).isAfter(newEvent.getEventDate())) {
                throw new PublicationException("Date can not be less than 2 hours before now");
            }
            EventStateAction newStateAction = newEvent.getStateAction();

            mapStates(newStateAction, event);

            if (newEvent.getLocation() != null) {
                Location location = locationRepository.findByLatAndLon(newEvent.getLocation().getLat(),
                        newEvent.getLocation().getLon());
                event.setLocation(location);
            }
            if (event.getState().equals(EventStatus.PUBLISHED)) {
                event.setPublishedOn(LocalDateTime.now());
            }
            return mapper.eventToEventFullDto(repository.save(event));

        } catch (ConstraintViolationException e) {
            throw new IntegrityConstraintViolationException("Нарушение ограничения");
        }
    }

    public List<RequestDto> getRequestsById(long userId, long eventId) {
        findById(eventId);
        findByIdUser(userId);
        return requestRepository.findByRequesterIdAndEventId(userId, eventId).stream()
                .map(request -> requestMapper.requestToRequestDto(request)).toList();
    }

    public EventRequestStatusUpdateResult modifyRequestStatus(long userId, long eventId,
                                                              EventRequestStatusUpdateRequest newRequest) {
        try {

            Event event = findById(eventId);
            if (event.getParticipationLimit() == event.getConfirmedRequests()) {
                throw new RequestLimitException("Participation limit has been reached");
            }
            List<Request> requests = newRequest.getRequestIds().stream().map(requestId -> requestRepository.findById(requestId)
                    .orElseThrow(() -> new NotFoundException("Request not found"))).toList();

            List<Request> confirmed = new ArrayList<>();
            List<Request> rejected = new ArrayList<>();
            if (!event.isRequestModeration() || event.getParticipationLimit() == 0) {
                return new EventRequestStatusUpdateResult(requests.stream()
                        .peek(request -> request.setStatus(RequestStatusType.CONFIRMED))
                        .map(requestMapper::requestToRequestDto).toList(), List.of());
            }
            for (Request request : requests) {
                if (!request.getStatus().equals(RequestStatusType.PENDING)) {
                    throw new PublicationException("Can not change not pending request");
                }
                if (event.getParticipationLimit() == event.getConfirmedRequests()) {
                    request.setStatus(RequestStatusType.REJECTED);
                    rejected.add(request);
                } else {
                    confirmed.add(request);
                }
            }
            return new EventRequestStatusUpdateResult(confirmed.stream().map(requestMapper::requestToRequestDto).toList(),
                    rejected.stream().map(requestMapper::requestToRequestDto).toList());


        } catch (ConstraintViolationException e) {
            throw new IntegrityConstraintViolationException("Нарушение ограничения");
        }
    }

    public List<EventShortDto> getAllPublic(String text, List<Long> categories, boolean paid, LocalDateTime rangeStart,
                                            LocalDateTime rangeEnd, boolean onlyAvailable, String sort, int from, int size) {
        int page = from / size;
        Pageable pageable = PageRequest.of(page, size);
        Page<Event> events = repository.findWithParamsPublic(text, categories, paid, rangeStart, rangeEnd, pageable);
        if (text == null && categories == null && rangeStart == null && rangeEnd == null) {
            return repository.findAll(pageable).stream().map(event -> mapper.eventToEventShortDto(event)).toList();
        }
        if (onlyAvailable) {
            return events.stream()
                    .filter(event -> event.getState().equals(EventStatus.PUBLISHED))
                    .filter(event -> event.getConfirmedRequests() != event.getParticipationLimit())
                    .map(event -> mapper.eventToEventShortDto(event)).toList();
        } else {
            return events.stream()
                    .filter(event -> event.getState().equals(EventStatus.PUBLISHED))
                    .filter(event -> event.getConfirmedRequests() == event.getParticipationLimit())
                    .map(event -> mapper.eventToEventShortDto(event)).toList();
        }
    }

    public EventFullDto getByIdPublic(long id) {
        return mapper.eventToEventFullDto(findById(id));
    }

    private Event findById(long eventId) {
        return repository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event with id=" + eventId + " not found"));
    }

    private void mapStates(EventStateAction stateAction, Event event) {
        if (stateAction != null) {
            Map<EventStateAction, EventStatus> states = Map.of(
                    EventStateAction.PUBLISH_EVENT, EventStatus.PUBLISHED,
                    EventStateAction.REJECT_EVENT, EventStatus.CANCELED,
                    EventStateAction.CANCEL_REVIEW, EventStatus.CANCELED,
                    EventStateAction.SEND_TO_REVIEW, EventStatus.PENDING
            );

            event.setState(states.get(stateAction));
        }

    }


    private User findByIdUser(long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id=" + userId + " not found"));
    }
}
