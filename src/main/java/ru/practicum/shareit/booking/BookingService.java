package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.model.BookingDto;
import ru.practicum.shareit.booking.model.BookingResponse;

import java.util.List;

public interface BookingService {
    BookingResponse createBooking(Long userId, BookingDto bookingDto);

    BookingResponse approveBooking(Long bookingId, String approved, Long userId);

    BookingResponse findById(Long bookingId, Long userId);

    List<BookingResponse> getBookingsByBooker(Long userId, String state);

    List<BookingResponse> getBookingsByOwner(Long userId, String state);

    List<BookingResponse> allBookingsByBooker(Long bookerId);

    List<BookingResponse> allBookingsByOwner(Long userId);

}
