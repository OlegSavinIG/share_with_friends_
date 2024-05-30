package ru.practicum.shareit.item.dto;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingMapper;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.comment.Comment;
import ru.practicum.shareit.item.comment.CommentDto;
import ru.practicum.shareit.item.comment.CommentMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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

    public static ItemDto mapToItemDtoWithBooking(Item item) {
        List<Booking> bookings = item.getBookings().stream()
                .filter(booking -> !booking.getStatus().equals(BookingStatus.REJECTED))
                .filter(booking -> !booking.getStatus().equals(BookingStatus.PAST))
                .sorted(Comparator.comparing(Booking::getStart))
                .collect(Collectors.toList());

        List<Comment> comments = item.getComments();

        ItemDto.ItemDtoBuilder itemDtoBuilder = ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable());
        if (!bookings.isEmpty()) {
            itemDtoBuilder.lastBooking(BookingMapper.mapToBookingResponseWithItem(bookings.get(0)));
//            itemDtoBuilder.lastBooking(BookingMapper.mapToBookingResponseWithItem(bookings.stream()
//                    .filter(booking -> booking.getStatus().equals(BookingStatus.CURRENT))
//                    .findFirst().get()));
            if (bookings.size() > 1) {
                itemDtoBuilder.nextBooking(BookingMapper.mapToBookingResponseWithItem(bookings.get(1)));
//                itemDtoBuilder.nextBooking(BookingMapper.mapToBookingResponseWithItem(bookings.stream()
//                        .filter(booking -> booking.getStatus().equals(BookingStatus.FUTURE))
////                                .filter(booking -> !booking.getStatus().equals(BookingStatus.CURRENT))
//                        .sorted(Comparator.comparing(Booking::getStart))
//                        .findFirst().get()));
            }
        }
        if (!comments.isEmpty()) {
            List<CommentDto> commentDtos = comments.stream()
                    .map(comment -> CommentMapper.mapToCommentDto(comment))
                    .collect(Collectors.toList());
            itemDtoBuilder.comments(commentDtos);
        } else {
            itemDtoBuilder.comments(Collections.emptyList());
        }
        return itemDtoBuilder.build();
    }


    public static Item itemDtoToSaveItem(ItemDto itemDto, User user) {
        return Item.builder()
                .name(itemDto.getName())
                .description(itemDto.getDescription())
                .available(itemDto.getAvailable())
                .user(user)
                .build();
    }

    public static ItemDto mapToItemDtoWithoutBooking(Item item) {
        List<Comment> comments = item.getComments();
        if (!comments.isEmpty()) {
            List<CommentDto> commentDtos = comments.stream()
                    .map(comment -> CommentMapper.mapToCommentDto(comment))
                    .collect(Collectors.toList());
            return ItemDto.builder()
                    .id(item.getId())
                    .name(item.getName())
                    .description(item.getDescription())
                    .available(item.getAvailable())
                    .comments(commentDtos)
                    .build();
        }
        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .comments(Collections.emptyList())
                .build();
    }
}
