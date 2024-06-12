package ru.practicum.shareit.booking.model;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.item.model.ItemDto;
import ru.practicum.shareit.user.model.UserDto;

import java.time.LocalDateTime;

@Data
@Builder
public class BookingResponse {
    private Long id;
    private LocalDateTime start;
    private LocalDateTime end;
    private BookingStatus status;
    private UserDto booker;
    private ItemDto item;
}
