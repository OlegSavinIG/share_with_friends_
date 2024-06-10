package ru.practicum.shareit.exception;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler globalExceptionHandler;

    @BeforeEach
    public void setup() {
        globalExceptionHandler = new GlobalExceptionHandler();
    }

    @Test
    public void testHandleValidationException() {
        ValidationException exception = new ValidationException("Ошибка ValidationException");
        String response = globalExceptionHandler.handleValidationException(exception);
        assertThat(response).isEqualTo("Ошибка ValidationException");
    }

    @Test
    public void testHandleNotExistException() {
        NotExistException exception = new NotExistException("Ошибка NotExistException");
        String response = globalExceptionHandler.handleNotExistException(exception);
        assertThat(response).isEqualTo("Ошибка NotExistException");
    }

    @Test
    public void testHandleDataNotFoundException() {
        DataNotFoundException exception = new DataNotFoundException("Ошибка DataNotFoundException");
        String response = globalExceptionHandler.handleDataNotFoundException(exception);
        assertThat(response).isEqualTo("Ошибка DataNotFoundException");
    }

    @Test
    public void testHandleUnsupportedStatusException() {
        UnsupportedStatusException exception = new UnsupportedStatusException("Ошибка");
        ResponseEntity<Map<String, String>> responseEntity = globalExceptionHandler.handleUnsupportedStatusException(exception);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        Map<String, String> expectedResponse = new HashMap<>();
        expectedResponse.put("error", "Unknown state: UNSUPPORTED_STATUS");
        assertThat(responseEntity.getBody()).isEqualTo(expectedResponse);
    }

    @Test
    public void testHandleBookingException() {
        BookingException exception = new BookingException("Ошибка BookingException");
        String response = globalExceptionHandler.handleBookingException(exception);
        assertThat(response).isEqualTo("Ошибка BookingException");
    }
}
