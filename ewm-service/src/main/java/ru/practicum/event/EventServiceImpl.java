package ru.practicum.event;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.practicum.category.Category;
import ru.practicum.category.CategoryRepository;
import ru.practicum.event.dto.*;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.EventStateAction;
import ru.practicum.event.model.EventStatus;
import ru.practicum.event.model.Location;
import ru.practicum.exception.*;
import ru.practicum.request.*;
import ru.practicum.stats.client.StatsClient;
import ru.practicum.stats.dto.HitRequest;
import ru.practicum.stats.dto.StatsResponse;
import ru.practicum.user.User;
import ru.practicum.user.UserRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Stream;

@Service
@AllArgsConstructor
@Slf4j
public class EventServiceImpl implements EventService {
    private EventMapper mapper;
    private LocationMapper locationMapper;
    private EventRepository repository;
    private UserRepository userRepository;
    private RequestRepository requestRepository;
    private RequestMapper requestMapper;
    private CategoryRepository categoryRepository;
    private StatsClient statsClient;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final Map<Long, Set<String>> uniqueIdsForEvents = new HashMap<>();

    public List<EventFullDto> getAllAdmin(List<Long> users, List<String> states, List<Long> categories, LocalDateTime rangeStart,
                                          LocalDateTime rangeEnd, int from, int size) {
        if (rangeStart != null && rangeEnd != null && rangeEnd.isBefore(rangeStart)) {
            throw new ValidationException("End must not be before start");
        }
        int page = from / size;
        Pageable pageable = PageRequest.of(page, size);
        Specification<Event> specification = Specification
                .where(EventSpecification.hasInitiators(users))
                .and(EventSpecification.hasStates(states))
                .and(EventSpecification.categoryIn(categories))
                .and(EventSpecification.eventDateAfter(rangeStart))
                .and(EventSpecification.eventDateBefore(rangeEnd));
        Page<Event> events = repository.findAll(specification, pageable);

        log.info("getAllAdmin method finished");
        return events.stream().map(event -> mapper.eventToEventFullDto(event)).toList();
    }

    public EventFullDto modifyEventAdmin(long eventId, UpdateEventAdminRequest newEvent) {

        Event updatedEvent = findById(eventId);

        if (updatedEvent.getState().equals(EventStatus.PUBLISHED) &&
                updatedEvent.getEventDate().isAfter(updatedEvent.getPublishedOn().plusHours(1))) {
            throw new AlreadyExistsException("For the requested operation the conditions are not met");
        }
        if (newEvent.getStateAction() != null) {

            EventStateAction newStateAction = getEventStateAction(newEvent, updatedEvent);
            mapStates(newStateAction, updatedEvent);
        }

        if (newEvent.getAnnotation() != null) {
            updatedEvent.setAnnotation(newEvent.getAnnotation());
        }
        if (newEvent.getDescription() != null) {
            updatedEvent.setDescription(newEvent.getDescription());
        }
        if (newEvent.getRequestModeration() != null) {
            updatedEvent.setRequestModeration(newEvent.getRequestModeration());
        }
        if (newEvent.getTitle() != null) {
            updatedEvent.setTitle(newEvent.getTitle());
        }
        if (newEvent.getParticipantLimit() != null) {
            updatedEvent.setParticipantLimit(newEvent.getParticipantLimit());
        }
        if (newEvent.getPaid() != null) {
            updatedEvent.setPaid(newEvent.getPaid());
        }
        if (newEvent.getEventDate() != null) {
            try {
                if (LocalDateTime.now().plusHours(2).isAfter(LocalDateTime
                        .parse(newEvent.getEventDate(), FORMATTER))) {
                    throw new ValidationException("Date can not be less than 2 hours before now");
                }
                updatedEvent.setEventDate(LocalDateTime
                        .parse(newEvent.getEventDate(), FORMATTER));
            } catch (DateTimeParseException ignored) {
            }
        }

        if (newEvent.getLocation() != null) {
            Location location = locationMapper.locationDtoToLocation(newEvent.getLocation());
            updatedEvent.setLocation(location);
        }
        if (updatedEvent.getState().equals(EventStatus.PUBLISHED)) {
            updatedEvent.setPublishedOn(LocalDateTime.now());
        }
        log.info("modifyEventAdmin method finished, id={}", eventId);

        return mapper.eventToEventFullDto(repository.save(updatedEvent));


    }

    private static EventStateAction getEventStateAction(UpdateEventAdminRequest newEvent, Event updatedEvent) {
        EventStateAction newStateAction = newEvent.getStateAction();
        if (!updatedEvent.getState().equals(EventStatus.PENDING) &&
                newStateAction.equals(EventStateAction.PUBLISH_EVENT)) {
            throw new PublicationException("Event is not in pending stage");
        }

        if (newStateAction.equals(EventStateAction.REJECT_EVENT) && updatedEvent.getState().equals(EventStatus.PUBLISHED)) {
            throw new PublicationException("Can not reject published event");
        }
        return newStateAction;
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
                throw new ValidationException("Date can not be less than 2 hours before now");
            }
            Category category = categoryRepository.findById(newEvent.getCategory())
                    .orElseThrow(() -> new NotFoundException("Category not found"));
            Event event = mapper.newEventToEvent(newEvent);
            event.setInitiator(user);
            event.setCategory(category);
            repository.save(event);
            log.info("addEvent method finished, id={}", event.getId());
            return mapper.eventToEventFullDto(event);
        } catch (DataIntegrityViolationException e) {
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
            Event updatedEvent = findById(eventId);

            if (updatedEvent.getState().equals(EventStatus.PUBLISHED)) {
                throw new EventAlreadyPublished("Can not change published event");
            }

            try {
                if (newEvent.getEventDate() != null && LocalDateTime.now().plusHours(2).isAfter(LocalDateTime
                        .parse(newEvent.getEventDate(), FORMATTER))) {
                    throw new ValidationException("Date can not be less than 2 hours before now");
                }
            } catch (DateTimeParseException ignored) {
            }

            EventStateAction newStateAction = newEvent.getStateAction();

            mapStates(newStateAction, updatedEvent);

            if (newEvent.getCategory() != null) {
                updatedEvent.setCategory(categoryRepository.findById(newEvent.getCategory())
                        .orElseThrow(() -> new NotFoundException("Category not found")));
            }
            if (newEvent.getLocation() != null) {
                Location location = locationMapper.locationDtoToLocation(newEvent.getLocation());
                updatedEvent.setLocation(location);
            }
            if (updatedEvent.getState().equals(EventStatus.PUBLISHED)) {
                updatedEvent.setPublishedOn(LocalDateTime.now());
            }
            if (newEvent.getParticipantLimit() != null) {
                if (newEvent.getParticipantLimit() < 0) {
                    throw new ValidationException("Лимит меньше 0");
                }
                updatedEvent.setParticipantLimit(newEvent.getParticipantLimit());
            }
            log.info("modifyEventByUser method finished, id={}", eventId);

            return mapper.eventToEventFullDto(repository.save(updatedEvent));

        } catch (DataIntegrityViolationException e) {
            throw new IntegrityConstraintViolationException("Нарушение ограничения");
        }
    }

    public List<RequestDto> getRequestsById(long userId, long eventId) {
        findById(eventId);
        findByIdUser(userId);
        return requestRepository.findAllByEventId(eventId).stream()
                .map(request -> requestMapper.requestToRequestDto(request)).toList();
    }

    public EventRequestStatusUpdateResult modifyRequestStatus(long userId, long eventId,
                                                              EventRequestStatusUpdateRequest newRequest) {
        try {

            Event event = findById(eventId);
            if (event.getState().equals(EventStatus.PENDING)) {
                throw new EventNotPublishedException("Event not published");
            }

            List<Request> requests = newRequest.getRequestIds().stream().map(requestId -> requestRepository.findById(requestId)
                    .orElseThrow(() -> new NotFoundException("Request not found"))).toList();

            List<Request> confirmed = new ArrayList<>();
            List<Request> rejected = new ArrayList<>();
            if (!event.isRequestModeration() && event.getParticipantLimit() == 0) {
                return new EventRequestStatusUpdateResult(requests.stream()
                        .peek(request -> request.setStatus(RequestStatusType.CONFIRMED))
                        .map(requestMapper::requestToRequestDto).toList(), List.of());
            }
            if (newRequest.getStatus().equals(RequestStatusType.CONFIRMED) &&
                    event.getParticipantLimit() != 0 && event.getParticipantLimit() <= event.getConfirmedRequests()) {
                throw new RequestLimitException("Participation limit has been reached");
            }
            for (Request request : requests) {
                if (!request.getStatus().equals(RequestStatusType.PENDING)) {
                    throw new PublicationException("Can not change not pending request");
                }
                if (newRequest.getStatus().equals(RequestStatusType.REJECTED)) {
                    request.setStatus(RequestStatusType.REJECTED);
                    rejected.add(request);
                } else if (event.getParticipantLimit() <= event.getConfirmedRequests()) {
                    request.setStatus(RequestStatusType.REJECTED);
                    rejected.add(request);
                } else {
                    request.setStatus(RequestStatusType.CONFIRMED);
                    confirmed.add(request);
                    event.setConfirmedRequests(event.getConfirmedRequests() + 1);
                }
            }
            requestRepository.saveAll(Stream.concat(confirmed.stream(), rejected.stream()).toList());
            if (!confirmed.isEmpty()) {
                repository.save(event);
            }
            log.info("modifyRequestStatus method finished, id={}", eventId);

            return new EventRequestStatusUpdateResult(confirmed.stream().map(requestMapper::requestToRequestDto).toList(),
                    rejected.stream().map(requestMapper::requestToRequestDto).toList());


        } catch (DataIntegrityViolationException e) {
            throw new IntegrityConstraintViolationException("Нарушение ограничения");
        }
    }

    public List<EventShortDto> getAllPublic(String text, List<Long> categories, boolean paid, LocalDateTime rangeStart,
                                            LocalDateTime rangeEnd, boolean onlyAvailable, String sort, int from, int size,
                                            HttpServletRequest httpServletRequest) {
        int page = from / size;
        Sort sortBy = Sort.unsorted();
        if ("EVENT_DATE".equals(sort)) {
            sortBy = Sort.by("eventDate").ascending();
        } else if ("VIEWS".equals(sort)) {
            sortBy = Sort.by("views").descending();
        }

        if (rangeEnd != null) {


            if (rangeEnd.isBefore(rangeStart)) {
                throw new ValidationException("End is before start");
            }
        }
        Pageable pageable = PageRequest.of(page, size, sortBy);

        Specification<Event> specification = Specification
                .where(EventSpecification.textInAnnotationOrDescription(text))
                .and(EventSpecification.categoryIn(categories))
                .and(EventSpecification.eventDateAfter(rangeStart))
                .and(EventSpecification.eventDateBefore(rangeEnd))
                .and(EventSpecification.isAvailable(onlyAvailable))
                .and(EventSpecification.sortBySortType(sort))
                .and(EventSpecification.onlyPublished());
        Page<Event> events = repository.findAll(specification, pageable);
        String ip = httpServletRequest.getRemoteAddr();
        events.forEach(event -> saveViewToEvent(ip, event));
        saveView("/events", ip);

        return events.stream().map(event -> mapper.eventToEventShortDto(event))
                .toList();
    }

    public EventFullDto getByIdPublic(long id, HttpServletRequest httpServletRequest) {
        Event event = repository.findByIdAndState(id, EventStatus.PUBLISHED)
                .orElseThrow(() -> new NotFoundException("Published event with id=" + id + " not found"));
        saveView("/events/" + id, httpServletRequest.getRemoteAddr());
        saveViewToEvent(httpServletRequest.getRemoteAddr(), event);
        List<StatsResponse> getResponses = loadStatsFromClient(
                event.getPublishedOn(),
                LocalDateTime.now(),
                List.of("/events/" + id),
                true);
        if (!getResponses.isEmpty()) {
            StatsResponse statsResponse = getResponses.getFirst();
            event.setViews(statsResponse.getHits());
        }
        return mapper.eventToEventFullDto(repository.save(event));
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

    private List<StatsResponse> loadStatsFromClient(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {
        return statsClient.stats(start, end, uris, unique);
    }

    private void saveViewToEvent(String ip, Event event) {
        Set<String> ipsForEvent = uniqueIdsForEvents.computeIfAbsent(event.getId(), k -> new HashSet<>());

        if (!ipsForEvent.contains(ip)) {
            event.setViews(event.getViews() + 1);
            ipsForEvent.add(ip);
            repository.save(event);
        }
    }

    private void saveView(String uri, String ip) {
        HitRequest hitRequest = new HitRequest();
        hitRequest.setApp("ewm-service");
        hitRequest.setIp(ip);
        hitRequest.setUri(uri);
        hitRequest.setTimestamp(LocalDateTime.now());
        statsClient.hit(hitRequest);
    }
}
