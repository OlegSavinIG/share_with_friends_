package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class BookingStatusCheckerTest {

    private Booking booking;

    @BeforeEach
    public void setup() {
        booking = new Booking();
    }

    @Test
    public void testSetBookingTimeStatusFuture() {
        LocalDateTime now = LocalDateTime.now();
        booking.setStart(now.plusDays(1));
        booking.setEnd(now.plusDays(2));

        BookingStatusChecker.setBookingTimeStatus(booking);

        assertEquals(BookingStatus.FUTURE, booking.getTimeStatus());
    }

    @Test
    public void testSetBookingTimeStatusCurrent() {
        LocalDateTime now = LocalDateTime.now();
        booking.setStart(now.minusDays(1));
        booking.setEnd(now.plusDays(1));

        BookingStatusChecker.setBookingTimeStatus(booking);

        assertEquals(BookingStatus.CURRENT, booking.getTimeStatus());
    }

    @Test
    public void testSetBookingTimeStatusPast() {
        LocalDateTime now = LocalDateTime.now();
        booking.setStart(now.minusDays(2));
        booking.setEnd(now.minusDays(1));

        BookingStatusChecker.setBookingTimeStatus(booking);

        assertEquals(BookingStatus.PAST, booking.getTimeStatus());
    }
}
