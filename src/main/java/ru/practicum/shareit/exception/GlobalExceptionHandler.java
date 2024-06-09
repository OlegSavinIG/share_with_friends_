package ru.practicum.shareit.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ru.practicum.shareit.exception.ValidationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public String handleValidationException(ValidationException e) {
        return e.getMessage();
    }

    @ExceptionHandler(NotExistException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleNotExistException(NotExistException e) {
        return e.getMessage();
    }

    @ExceptionHandler(DataNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleDataNotFoundException(DataNotFoundException e) {
        return e.getMessage();
    }

    @ExceptionHandler(UnsupportedStatusException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Map<String, String>> handleUnsupportedStatusException(UnsupportedStatusException e) {
        Map<String, String> response = new HashMap<>();
        response.put("error", "Unknown state: UNSUPPORTED_STATUS");
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ru.practicum.shareit.exception.BookingException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleBookingException(BookingException e) {
        return e.getMessage();
    }

}
