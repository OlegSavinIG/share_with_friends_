package ru.practicum.shareit.booking.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class BookingResponseWithItem {
    private Long id;
    private LocalDateTime start;
    //    private LocalDateTime end;
    private BookingStatus status;
    private Long bookerId;
}
