package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.model.BookingDto;
import ru.practicum.shareit.booking.model.BookingResponse;

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
    public ResponseEntity<BookingResponse> createBooking(@RequestHeader(name = "X-Sharer-User-Id") Long bookerId,
                                                         @RequestBody BookingDto bookingDto) {
        return ResponseEntity.ok(bookingService.createBooking(bookerId, bookingDto));
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<BookingResponse> approveBooking(@PathVariable Long bookingId,
                                                          @RequestParam String approved,
                                                          @RequestHeader(name = "X-Sharer-User-Id") Long ownerId) {
//        if (!approved.equals("true") && !approved.equals("false")) {
//            throw new ValidationException("Неправильно передан параметр approved");
//        }
        return ResponseEntity.ok(bookingService.approveBooking(bookingId, approved, ownerId));
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<BookingResponse> findById(@PathVariable Long bookingId,
                                                    @RequestHeader(name = "X-Sharer-User-Id") Long userId) {
        return ResponseEntity.ok(bookingService.findById(bookingId, userId));
    }

    @GetMapping
    public ResponseEntity<List<BookingResponse>> allBookingsByBooker(@RequestHeader(name = "X-Sharer-User-Id") Long bookerId,
                                                                     @RequestParam(required = false) String state,
                                                                     @RequestParam(defaultValue = "0") Integer from,
                                                                     @RequestParam(defaultValue = "10") Integer size) {
        if (state == null || state.equalsIgnoreCase("all")) {
            return ResponseEntity.ok(bookingService.allBookingsByBooker(bookerId, from, size));
        }
//        boolean anyMatchStatus = Arrays.stream(BookingStatus.values())
//                .anyMatch(bookingStatus -> bookingStatus.name().equalsIgnoreCase(state));
//        if (!anyMatchStatus) {
//            throw new UnsupportedStatusException("Unsupported status: " + state);
//        }
        return ResponseEntity.ok(bookingService.getBookingsByBooker(bookerId, state, from, size));
    }

    @GetMapping("/owner")
    public ResponseEntity<List<BookingResponse>> allBookingsByOwner(@RequestHeader(name = "X-Sharer-User-Id") Long ownerId,
                                                                    @RequestParam(required = false) String state,
                                                                    @RequestParam(defaultValue = "0") Integer from,
                                                                    @RequestParam(defaultValue = "10") Integer size) {
        if (state == null || state.equalsIgnoreCase("all")) {
            return ResponseEntity.ok(bookingService.allBookingsByOwner(ownerId, from, size));
        }
//        boolean anyMatchStatus = Arrays.stream(BookingStatus.values())
//                .anyMatch(bookingStatus -> bookingStatus.name().equalsIgnoreCase(state));
//        if (!anyMatchStatus) {
//            throw new UnsupportedStatusException("Unknown state: " + state);
//        }
        return ResponseEntity.ok(bookingService.getBookingsByOwner(ownerId, state, from, size));
    }

}
