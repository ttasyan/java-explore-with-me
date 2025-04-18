package ru.practicum.request;



import java.util.List;

public interface RequestService {
    List<RequestDto> getById(long userId);

    RequestDto cancelRequest(long userId, long requestId);

    RequestDto addRequest(long userId, long eventId);
}
