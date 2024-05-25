package ru.practicum.shareit.item.dto;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.Comparator;
import java.util.TreeSet;

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
       TreeSet<Booking> bookings = new TreeSet<>(Comparator.comparing(Booking::getEnd));
       bookings.addAll(item.getBookings());
        if (bookings.isEmpty()) {
            return ItemDtoWithBooking.builder()
                    .id(item.getId())
                    .name(item.getName())
                    .description(item.getDescription())
                    .available(item.getAvailable())
                    .build();
        }
        return ItemDtoWithBooking.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .lastBookerId(bookings.first().getBooker().getId())
                .nextBookerId(bookings.last().getBooker().getId())
                .lastBookingId(bookings.first().getId())
                .nextBookingId(bookings.last().getId())
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
