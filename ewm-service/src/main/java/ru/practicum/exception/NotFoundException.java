package ru.practicum.exception;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String m) {
        super(m);
    }
}
