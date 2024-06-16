package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.practicum.shareit.booking.dto.BookItemRequestDto;
import ru.practicum.shareit.booking.dto.BookingState;
import ru.practicum.shareit.exception.ValidationException;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class BookingControllerTest {

    @Mock
    private BookingClient bookingClient;

    @InjectMocks
    private BookingController bookingController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void bookItemSuccess() {
        long userId = 1L;
        BookItemRequestDto requestDto = BookItemRequestDto.builder()
                .itemId(1L)
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .build();
        ResponseEntity<Object> expectedResponse = new ResponseEntity<>(HttpStatus.OK);

        when(bookingClient.bookItem(anyLong(), any(BookItemRequestDto.class))).thenReturn(expectedResponse);

        ResponseEntity<Object> response = bookingController.bookItem(userId, requestDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(bookingClient, times(1)).bookItem(userId, requestDto);
    }

    @Test
    void approveBookingSuccess() {
        Long bookingId = 1L;
        String approved = "true";
        Long ownerId = 1L;
        ResponseEntity<Object> expectedResponse = new ResponseEntity<>(HttpStatus.OK);

        when(bookingClient.approveBooking(anyLong(), anyString(), anyLong())).thenReturn(expectedResponse);

        ResponseEntity<Object> response = bookingController.approveBooking(bookingId, approved, ownerId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(bookingClient, times(1)).approveBooking(bookingId, approved, ownerId);
    }

    @Test
    void approveBookingInvalidApprovedParam() {
        Long bookingId = 1L;
        String approved = "invalid";
        Long ownerId = 1L;

        ValidationException thrown = assertThrows(ValidationException.class, () -> {
            bookingController.approveBooking(bookingId, approved, ownerId);
        });

        assertEquals("Неправильно передан параметр approved", thrown.getMessage());
    }

    @Test
    void getBookingsSuccess() {
        long userId = 1L;
        String stateParam = "all";
        Integer from = 0;
        Integer size = 10;
        BookingState state = BookingState.ALL;
        ResponseEntity<Object> expectedResponse = new ResponseEntity<>(HttpStatus.OK);

        when(bookingClient.getBookings(anyLong(), any(BookingState.class), anyInt(), anyInt())).thenReturn(expectedResponse);

        ResponseEntity<Object> response = bookingController.getBookings(userId, stateParam, from, size);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(bookingClient, times(1)).getBookings(userId, state, from, size);
    }

    @Test
    void getBookingsInvalidStateParam() {
        long userId = 1L;
        String stateParam = "invalid";
        Integer from = 0;
        Integer size = 10;

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            bookingController.getBookings(userId, stateParam, from, size);
        });

        assertEquals("Unknown state: invalid", thrown.getMessage());
    }

    @Test
    void allBookingsByOwnerSuccess() {
        Long ownerId = 1L;
        String stateParam = "all";
        Integer from = 0;
        Integer size = 10;
        BookingState state = BookingState.ALL;
        ResponseEntity<Object> expectedResponse = new ResponseEntity<>(HttpStatus.OK);

        when(bookingClient.allBookingsByOwner(anyLong(), any(BookingState.class), anyInt(), anyInt())).thenReturn(expectedResponse);

        ResponseEntity<Object> response = bookingController.allBookingsByOwner(ownerId, stateParam, from, size);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(bookingClient, times(1)).allBookingsByOwner(ownerId, state, from, size);
    }

    @Test
    void allBookingsByOwnerInvalidStateParam() {
        Long ownerId = 1L;
        String stateParam = "invalid";
        Integer from = 0;
        Integer size = 10;

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            bookingController.allBookingsByOwner(ownerId, stateParam, from, size);
        });

        assertEquals("Unknown state: invalid", thrown.getMessage());
    }

    @Test
    void getBookingSuccess() {
        long userId = 1L;
        Long bookingId = 1L;
        ResponseEntity<Object> expectedResponse = new ResponseEntity<>(HttpStatus.OK);

        when(bookingClient.getBooking(anyLong(), anyLong())).thenReturn(expectedResponse);

        ResponseEntity<Object> response = bookingController.getBooking(userId, bookingId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(bookingClient, times(1)).getBooking(userId, bookingId);
    }
}
