package ru.practicum.shareit.booking;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BookingStatusChecker {
    public static void setBookingTimeStatus(Booking booking) {
//        if (booking.getStatus().equals(BookingStatus.WAITING)) {
//           booking.setStatus(BookingStatus.WAITING);
//        }
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime start = booking.getStart();
        LocalDateTime end = booking.getEnd();
        if (start.isAfter(now) && end.isAfter(now)) {
            booking.setTimeStatus(BookingStatus.FUTURE);
        }
        if (start.isBefore(now) && end.isAfter(now)) {
            booking.setTimeStatus(BookingStatus.CURRENT);
        }
        if (start.isBefore(now) && end.isBefore(now)) {
            booking.setTimeStatus(BookingStatus.PAST);
        }

    }
}
