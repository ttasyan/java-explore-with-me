package ru.practicum.exception;

public class EventAlreadyPublished extends RuntimeException {
    public EventAlreadyPublished(String m) {
        super(m);
    }
}
