package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.model.BookingDto;
import ru.practicum.shareit.booking.model.BookingResponse;

import java.util.List;

public interface BookingService {
    BookingResponse createBooking(Long userId, BookingDto bookingDto);

    BookingResponse approveBooking(Long bookingId, String approved, Long userId);

    BookingResponse findById(Long bookingId, Long userId);

    List<BookingResponse> allBookingsByBookerAndStatus(String state, Long userId);
    List<BookingResponse> allBookingsByBooker(Long bookerId);

    List<BookingResponse> allBookingsByOwnerAndStatus(String state, Long userId);
    List<BookingResponse> allBookingsByOwner(Long userId);

}
