package ru.practicum.exception;

public class DuplicatedDataException extends RuntimeException {
    public DuplicatedDataException(String m) {
        super(m);
    }
}
