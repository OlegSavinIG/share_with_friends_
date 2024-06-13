package ru.practicum.shareit.booking.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BookingStatusTest {

    @Test
    public void testBookingStatusValues() {
        BookingStatus[] statuses = BookingStatus.values();

        assertEquals(7, statuses.length);

        assertTrue(containsStatus(statuses, BookingStatus.CURRENT));
        assertTrue(containsStatus(statuses, BookingStatus.WAITING));
        assertTrue(containsStatus(statuses, BookingStatus.APPROVED));
        assertTrue(containsStatus(statuses, BookingStatus.REJECTED));
        assertTrue(containsStatus(statuses, BookingStatus.PAST));
        assertTrue(containsStatus(statuses, BookingStatus.FUTURE));
        assertTrue(containsStatus(statuses, BookingStatus.ALL));
    }

    @Test
    public void testBookingStatusValueOf() {
        // Проверка получения значения по строке
        assertEquals(BookingStatus.CURRENT, BookingStatus.valueOf("CURRENT"));
        assertEquals(BookingStatus.WAITING, BookingStatus.valueOf("WAITING"));
        assertEquals(BookingStatus.APPROVED, BookingStatus.valueOf("APPROVED"));
        assertEquals(BookingStatus.REJECTED, BookingStatus.valueOf("REJECTED"));
        assertEquals(BookingStatus.PAST, BookingStatus.valueOf("PAST"));
        assertEquals(BookingStatus.FUTURE, BookingStatus.valueOf("FUTURE"));
        assertEquals(BookingStatus.ALL, BookingStatus.valueOf("ALL"));
    }

    private boolean containsStatus(BookingStatus[] statuses, BookingStatus status) {
        for (BookingStatus s : statuses) {
            if (s == status) {
                return true;
            }
        }
        return false;
    }
}
