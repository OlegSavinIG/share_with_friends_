package ru.practicum.shareit.booking.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.model.UserDtoMapper;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BookingMapperTest {

    private Booking booking;
    private BookingDto bookingDto;
    private Item item;
    private User owner;
    private User booker;

    @BeforeEach
    public void setup() {
        owner = User.builder()
                .id(1L)
                .name("Owner")
                .email("owner@example.com")
                .build();

        booker = User.builder()
                .id(2L)
                .name("Booker")
                .email("booker@example.com")
                .build();

        item = Item.builder()
                .id(1L)
                .name("Item")
                .description("Description")
                .available(true)
                .user(owner)
                .build();

        bookingDto = BookingDto.builder()
                .id(1L)
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .status(BookingStatus.WAITING)
                .itemId(item.getId())
                .build();

        booking = Booking.builder()
                .id(1L)
                .start(bookingDto.getStart())
                .end(bookingDto.getEnd())
                .status(BookingStatus.WAITING)
                .item(item)
                .owner(owner)
                .booker(booker)
                .build();
    }

    @Test
    public void testMapToBooking() {
        Booking mappedBooking = BookingMapper.mapToBooking(item, booker, owner, bookingDto);
        assertEquals(booking.getStart(), mappedBooking.getStart());
        assertEquals(booking.getOwner(), mappedBooking.getOwner());
        assertEquals(booking.getBooker(), mappedBooking.getBooker());
        assertEquals(booking.getEnd(), mappedBooking.getEnd());
        assertEquals(booking.getStatus(), mappedBooking.getStatus());
        assertEquals(booking.getItem().getId(), mappedBooking.getItem().getId());
    }

    @Test
    public void testMapToBookingDto() {
        BookingDto mappedBookingDto = BookingMapper.mapToBookingDto(booking);
        assertEquals(bookingDto.getId(), mappedBookingDto.getId());
        assertEquals(bookingDto.getStart(), mappedBookingDto.getStart());
        assertEquals(bookingDto.getEnd(), mappedBookingDto.getEnd());
        assertEquals(bookingDto.getStatus(), mappedBookingDto.getStatus());
        assertEquals(bookingDto.getItemId(), mappedBookingDto.getItemId());
    }

    @Test
    public void testMapToBookingResponse() {
        BookingResponse bookingResponse = BookingMapper.mapToBookingResponse(booking);
        assertEquals(booking.getId(), bookingResponse.getId());
        assertEquals(booking.getStart(), bookingResponse.getStart());
        assertEquals(booking.getEnd(), bookingResponse.getEnd());
        assertEquals(booking.getStatus(), bookingResponse.getStatus());
        assertEquals(UserDtoMapper.userToUserDto(booking.getBooker()).getId(), bookingResponse.getBooker().getId());
        assertEquals(ItemMapper.mapItemToItemDto(booking.getItem()).getId(), bookingResponse.getItem().getId());
    }

    @Test
    public void testMapToBookingResponseWithItem() {
        BookingResponseWithItem bookingResponseWithItem = BookingMapper.mapToBookingResponseWithItem(booking);
        assertEquals(booking.getId(), bookingResponseWithItem.getId());
        assertEquals(booking.getStart(), bookingResponseWithItem.getStart());
        assertEquals(booking.getEnd(), bookingResponseWithItem.getEnd());
        assertEquals(booking.getStatus(), bookingResponseWithItem.getStatus());
        assertEquals(booking.getBooker().getId(), bookingResponseWithItem.getBookerId());
    }
}
