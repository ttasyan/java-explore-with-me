package ru.practicum.exception;

public class IntegrityConstraintViolationException extends RuntimeException {
    public IntegrityConstraintViolationException(String m) {
        super(m);
    }
}
