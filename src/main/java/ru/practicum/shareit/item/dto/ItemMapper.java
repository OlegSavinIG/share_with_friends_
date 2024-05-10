package ru.practicum.shareit.item.dto;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ItemMapper {

    public static ItemDto mapItemToItemDto(Item item) {
        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .build();
    }
    public static ItemDtoWithBooking mapItemToItemDtoWithBooking(Item item) {
        return ItemDtoWithBooking.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .lastBookerId(item.getBookings().getFirst().getBooker().getId())
                .nextBookerId(item.getBookings().getLast().getBooker().getId())
                .lastBookingId(item.getBookings().getFirst().getId())
                .nextBookingId(item.getBookings().getLast().getId())
                .build();
    }

    public static Item itemDtoToSaveItem(ItemDto itemDto, User user) {
        return Item.builder()
                .name(itemDto.getName())
                .description(itemDto.getDescription())
                .available(itemDto.getAvailable())
                .user(user)
                .build();
    }
}
