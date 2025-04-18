package ru.practicum.exception;

public class ValidationException extends RuntimeException {
    public ValidationException(String m) {
        super(m);
    }
}
