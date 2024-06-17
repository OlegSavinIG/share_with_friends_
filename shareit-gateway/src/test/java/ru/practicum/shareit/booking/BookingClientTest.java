package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.booking.dto.BookItemRequestDto;
import ru.practicum.shareit.booking.dto.BookingState;

import java.util.Map;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class BookingClientTest {

    @Mock
    private RestTemplateBuilder builder;

    @Mock
    private RestTemplate restTemplate;

    private BookingClient bookingClient;

    private static final String SERVER_URL = "http://localhost:8080";

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        when(builder.uriTemplateHandler(any(DefaultUriBuilderFactory.class))).thenReturn(builder);
        when(builder.requestFactory(any(Supplier.class))).thenReturn(builder);
        when(builder.build()).thenReturn(restTemplate);

        bookingClient = new BookingClient(SERVER_URL, builder);
    }

    @Test
    void testGetBookings() {
        ResponseEntity<Object> response = new ResponseEntity<>(HttpStatus.OK);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(HttpEntity.class), eq(Object.class), anyMap())).thenReturn(response);

        ResponseEntity<Object> result = bookingClient.getBookings(1L, BookingState.ALL, 0, 10);

        assertNotNull(result);
        verify(restTemplate).exchange(
                eq("?state={state}&from={from}&size={size}"),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                eq(Object.class),
                eq(Map.of("state", "ALL", "from", 0, "size", 10))
        );
    }

    @Test
    void testBookItem() {
        ResponseEntity<Object> response = new ResponseEntity<>(HttpStatus.OK);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), any(HttpEntity.class), eq(Object.class))).thenReturn(response);

        BookItemRequestDto requestDto = new BookItemRequestDto();
        ResponseEntity<Object> result = bookingClient.bookItem(1L, requestDto);

        assertNotNull(result);
        verify(restTemplate).exchange(
                eq(""),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                eq(Object.class)
        );
    }

    @Test
    void testGetBooking() {
        ResponseEntity<Object> response = new ResponseEntity<>(HttpStatus.OK);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(HttpEntity.class), eq(Object.class))).thenReturn(response);

        ResponseEntity<Object> result = bookingClient.getBooking(1L, 1L);

        assertNotNull(result);
        verify(restTemplate).exchange(
                eq("/1"),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                eq(Object.class)
        );
    }

    @Test
    void testApproveBooking() {
        ResponseEntity<Object> response = new ResponseEntity<>(HttpStatus.OK);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.PATCH), any(HttpEntity.class), eq(Object.class))).thenReturn(response);

        ResponseEntity<Object> result = bookingClient.approveBooking(1L, "true", 1L);

        assertNotNull(result);
        verify(restTemplate).exchange(
                eq("/1?approved=true"),
                eq(HttpMethod.PATCH),
                any(HttpEntity.class),
                eq(Object.class)
        );
    }

    @Test
    void testAllBookingsByOwner() {
        ResponseEntity<Object> response = new ResponseEntity<>(HttpStatus.OK);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(HttpEntity.class), eq(Object.class), anyMap())).thenReturn(response);

        ResponseEntity<Object> result = bookingClient.allBookingsByOwner(1L, BookingState.ALL, 0, 10);

        assertNotNull(result);
        verify(restTemplate).exchange(
                eq("/owner?state={state}&from={from}&size={size}"),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                eq(Object.class),
                eq(Map.of("state", "ALL", "from", 0, "size", 10))
        );
    }
}
