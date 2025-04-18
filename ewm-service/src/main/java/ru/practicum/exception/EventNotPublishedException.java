package ru.practicum.exception;

public class EventNotPublishedException extends RuntimeException {
    public EventNotPublishedException(String m) {
        super(m);
    }
}
