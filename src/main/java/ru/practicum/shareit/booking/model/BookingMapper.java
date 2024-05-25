package ru.practicum.shareit.booking.model;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dto.UserDtoMapper;
import ru.practicum.shareit.user.model.User;
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BookingMapper {
    public static Booking mapToBooking(Item item, User booker, User owner, BookingDto bookingDto) {
        return Booking.builder()
                .owner(owner)
                .booker(booker)
                .item(item)
                .start(bookingDto.getStart())
                .end(bookingDto.getEnd())
                .status(BookingStatus.WAITING)
                .build();
    }
    public static BookingDto mapToBookingDto (Booking booking) {
        return BookingDto.builder()
                .id(booking.getId())
//                .booker(booking.getBooker())
                .end(booking.getEnd())
                .start(booking.getStart())
                .status(booking.getStatus())
                .itemId(booking.getItem().getId())
//                .itemName(booking.getItem().getName())
//                .item(booking.getItem())
                .build();
    }

    public static BookingResponse mapToBookingResponse(Booking booking) {
        return BookingResponse.builder()
                .id(booking.getId())
                .booker(UserDtoMapper.userToUserDto(booking.getBooker()))
                .end(booking.getEnd())
                .start(booking.getStart())
                .status(booking.getStatus())
                .item(ItemMapper.mapItemToItemDto(booking.getItem()))
                .build();
    }
}
