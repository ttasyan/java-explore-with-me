package ru.practicum.exception;

public class AlreadyExistsException extends RuntimeException {
    public AlreadyExistsException(String m) {
        super(m);
    }
}
