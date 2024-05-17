package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exception.ValidationException;

import java.util.List;

/**
 * TODO Sprint add-bookings.
 */
@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    private BookingDto createBooking(@RequestHeader(name = "X-Sharer-User-Id") Long bookerId,
                                     @RequestBody BookingDto bookingDto) {
        return bookingService.createBooking(bookerId, bookingDto);
    }

    @PatchMapping("/{bookingId}")
    private BookingDto approveBooking(@PathVariable Long bookingId,
                                      @RequestParam String approved,
                                      @RequestHeader(name = "X-Sharer-User-Id") Long ownerId) {
        if (approved.equals("true") || approved.equals("false")) {
            return bookingService.approveBooking(bookingId, approved, ownerId);
        } else throw new ValidationException("Неправильно передан параметр approved");
    }
    @GetMapping("/{bookingId}")
    private BookingDto findById(@PathVariable Long bookingId,
                                @RequestHeader(name = "X-Sharer-User-Id") Long userId) {
        return bookingService.findById(bookingId, userId);
    }

    @GetMapping
    private List<BookingDto> allBookingsByUser(@RequestHeader(name = "X-Sharer-User-Id") Long bookerId,
                                               @RequestParam(required = false) String state) {
        return bookingService.allBookingsByBooker(state, bookerId);
    }

    @GetMapping("/owner")
    private List<BookingDto> allBookingsByOwner(@RequestHeader(name = "X-Sharer-User-Id") Long ownerId,
                                                @RequestParam(required = false) String state) {
        return bookingService.allBookingsByOwner(state, ownerId);
    }
}
