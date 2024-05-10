package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class ItemDtoWithBooking extends ItemDto{
    private Long lastBookingId;
    private Long lastBookerId;
    private Long nextBookingId;
    private Long nextBookerId;
}
