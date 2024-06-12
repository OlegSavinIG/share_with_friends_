package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.model.BookingDto;
import ru.practicum.shareit.booking.model.BookingResponse;

import java.util.List;

public interface BookingService {
    BookingResponse createBooking(Long userId, BookingDto bookingDto);

    BookingResponse approveBooking(Long bookingId, String approved, Long userId);

    BookingResponse findById(Long bookingId, Long userId);

    List<BookingResponse> getBookingsByBooker(Long userId, String state, int from, int size);

    List<BookingResponse> getBookingsByOwner(Long userId, String state, int from, int size);

    List<BookingResponse> allBookingsByBooker(Long bookerId, int from, int size);

    List<BookingResponse> allBookingsByOwner(Long userId, int from, int size);

}
