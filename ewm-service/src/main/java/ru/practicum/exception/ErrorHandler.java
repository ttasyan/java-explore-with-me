package ru.practicum.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;


@RestControllerAdvice
public class ErrorHandler {
    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFound(NotFoundException e) {
        String reasonMessage = "Not found";
        return ErrorResponse.builder()
                .errors(List.of(e.getMessage()))
                .message(e.getMessage())
                .reason(reasonMessage)
                .status(HttpStatus.NOT_FOUND.toString())
                .build();
    }


    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationException(ValidationException e) {
        String reasonMessage = "Validation failed";
        return ErrorResponse.builder()
                .errors(List.of(e.getMessage()))
                .message(e.getMessage())
                .reason(reasonMessage)
                .status(HttpStatus.BAD_REQUEST.toString())
                .build();
    }

    @ExceptionHandler(AlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleAlreadyExistsException(AlreadyExistsException e) {
        String reasonMessage = "Field already exists";
        return ErrorResponse.builder()
                .errors(List.of(e.getMessage()))
                .message(e.getMessage())
                .reason(reasonMessage)
                .status(HttpStatus.CONFLICT.toString())
                .build();
    }

    @ExceptionHandler(EventAlreadyPublished.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleEventAlreadyPublished(EventAlreadyPublished e) {
        String reasonMessage = "Event already published";
        return ErrorResponse.builder()
                .errors(List.of(e.getMessage()))
                .message(e.getMessage())
                .reason(reasonMessage)
                .status(HttpStatus.CONFLICT.toString())
                .build();
    }

    @ExceptionHandler(EventNotPublishedException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleEventNotPublishedException(EventNotPublishedException e) {
        String reasonMessage = "Event not published";
        return ErrorResponse.builder()
                .errors(List.of(e.getMessage()))
                .message(e.getMessage())
                .reason(reasonMessage)
                .status(HttpStatus.CONFLICT.toString())
                .build();
    }

    @ExceptionHandler(InitiatorException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleInitiatorException(InitiatorException e) {
        String reasonMessage = "Initiator can not be requestor";
        return ErrorResponse.builder()
                .errors(List.of(e.getMessage()))
                .message(e.getMessage())
                .reason(reasonMessage)
                .status(HttpStatus.CONFLICT.toString())
                .build();
    }

    @ExceptionHandler(IntegrityConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleIntegrityConstraintViolationException(IntegrityConstraintViolationException e) {
        String reasonMessage = "Data integrity violation";
        return ErrorResponse.builder()
                .errors(List.of(e.getMessage()))
                .message(e.getMessage())
                .reason(reasonMessage)
                .status(HttpStatus.BAD_REQUEST.toString())
                .build();
    }

    @ExceptionHandler(PublicationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handlePublicationException(PublicationException e) {
        String reasonMessage = "For the requested operation the conditions are not met";
        return ErrorResponse.builder()
                .errors(List.of(e.getMessage()))
                .message(e.getMessage())
                .reason(reasonMessage)
                .status(HttpStatus.CONFLICT.toString())
                .build();
    }

    @ExceptionHandler(RequestLimitException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleRequestLimitException(RequestLimitException e) {
        String reasonMessage = "Participation limit has been reached";
        return ErrorResponse.builder()
                .errors(List.of(e.getMessage()))
                .message(e.getMessage())
                .reason(reasonMessage)
                .status(HttpStatus.CONFLICT.toString())
                .build();
    }

    @ExceptionHandler(DuplicatedDataException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleDuplicatedDataException(DuplicatedDataException e) {
        String reasonMessage = "Duplicated data";
        return ErrorResponse.builder()
                .errors(List.of(e.getMessage()))
                .message(e.getMessage())
                .reason(reasonMessage)
                .status(HttpStatus.CONFLICT.toString())
                .build();
    }

    @ExceptionHandler(CategoryHasEventsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleCategoryHasEventsException(CategoryHasEventsException e) {
        String reasonMessage = "Category has events";
        return ErrorResponse.builder()
                .errors(List.of(e.getMessage()))
                .message(e.getMessage())
                .reason(reasonMessage)
                .status(HttpStatus.CONFLICT.toString())
                .build();
    }

}
