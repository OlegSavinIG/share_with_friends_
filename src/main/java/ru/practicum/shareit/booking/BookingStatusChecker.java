package ru.practicum.shareit.booking;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingResponse;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BookingStatusChecker {
    public static BookingStatus getBookingStatus(BookingResponse bookingResponse) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime start = bookingResponse.getStart();
        LocalDateTime end = bookingResponse.getEnd();
        if (start.isBefore(now) && end.isAfter(now)) {
            bookingResponse.setStatus(BookingStatus.CURRENT);
        }
        if (end.isBefore(now)) {
            bookingResponse.setStatus(BookingStatus.PAST);
        }
        if (start.isAfter(now)) {
            bookingResponse.setStatus(BookingStatus.FUTURE);
        }
        return bookingResponse.getStatus();
    }
}
