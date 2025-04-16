package ru.practicum.exception;

public class RequestLimitException extends RuntimeException {
    public RequestLimitException(String m) {
        super(m);
    }
}
