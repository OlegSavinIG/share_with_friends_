package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;

import java.util.List;

public interface BookingService {
    BookingDto createBooking(Long userId, BookingDto bookingDto);

    BookingDto approveBooking(Long bookingId, String approved, Long userId);

    BookingDto findById(Long bookingId, Long userId);

    List<BookingDto> allBookingsByBooker(String state, Long userId);

    List<BookingDto> allBookingsByOwner(String state, Long userId);
}
