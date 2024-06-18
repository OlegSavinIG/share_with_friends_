package ru.practicum.shareit.booking.dto;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class BookingStateTest {

    @Test
    void testFromAll() {
        Optional<BookingState> state = BookingState.from("ALL");
        assertEquals(Optional.of(BookingState.ALL), state);
    }

    @Test
    void testFromCurrent() {
        Optional<BookingState> state = BookingState.from("CURRENT");
        assertEquals(Optional.of(BookingState.CURRENT), state);
    }

    @Test
    void testFromFuture() {
        Optional<BookingState> state = BookingState.from("FUTURE");
        assertEquals(Optional.of(BookingState.FUTURE), state);
    }

    @Test
    void testFromPast() {
        Optional<BookingState> state = BookingState.from("PAST");
        assertEquals(Optional.of(BookingState.PAST), state);
    }

    @Test
    void testFromRejected() {
        Optional<BookingState> state = BookingState.from("REJECTED");
        assertEquals(Optional.of(BookingState.REJECTED), state);
    }

    @Test
    void testFromWaiting() {
        Optional<BookingState> state = BookingState.from("WAITING");
        assertEquals(Optional.of(BookingState.WAITING), state);
    }

    @Test
    void testFromInvalid() {
        Optional<BookingState> state = BookingState.from("INVALID");
        assertFalse(state.isPresent());
    }
}
