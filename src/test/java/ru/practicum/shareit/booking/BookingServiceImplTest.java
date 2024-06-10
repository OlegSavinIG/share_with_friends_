package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingDto;
import ru.practicum.shareit.booking.model.BookingResponse;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.exception.NotExistException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BookingServiceImplTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private BookingServiceImpl bookingService;

    private BookingDto bookingDto;
    private User owner;
    private User booker;
    private Item item;
    private Booking booking;

    @BeforeEach
    void setUp() {
        owner = User.builder()
                .id(1L)
                .name("Test User")
                .email("testuser@example.com")
                .build();
        booker = User.builder()
                .id(2L)
                .name("Test User2")
                .email("testuser2@example.com")
                .build();

        item = Item.builder()
                .id(1L)
                .name("Test Item")
                .description("Test Description")
                .available(true)
                .user(owner)
                .build();

        bookingDto = BookingDto.builder()
                .itemId(1L)
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .build();

        booking = Booking.builder()
                .id(1L)
                .booker(booker)
                .owner(owner)
                .item(item)
                .start(bookingDto.getStart())
                .end(bookingDto.getEnd())
                .status(BookingStatus.WAITING)
                .build();
    }

    @Test
    void createBookingValid() {
        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(booker));
        when(itemRepository.existsById(anyLong())).thenReturn(true);
        when(itemRepository.findAvailableById(anyLong())).thenReturn(true);
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);

        BookingResponse response = bookingService.createBooking(2L, bookingDto);

        assertNotNull(response);
    }

    @Test
    void createBookingUserNotExist() {
        assertThrows(NotExistException.class, () -> bookingService.createBooking(1L, bookingDto));
    }

    @Test
    void createBookingItemNotExist() {
        when(itemRepository.existsById(anyLong())).thenReturn(false);

        assertThrows(NotExistException.class, () -> bookingService.createBooking(1L, bookingDto));
    }

    @Test
    void createBookingOwnerItem() {
        assertThrows(NotExistException.class, () -> bookingService.createBooking(1L, bookingDto));
    }

    @Test
    void approveBookingValid() {
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(booking));
        when(bookingRepository.existsById(anyLong())).thenReturn(true);
        when(userRepository.existsById(anyLong())).thenReturn(true);

        BookingResponse response = bookingService.approveBooking(1L, "true", 1L);

        assertNotNull(response);
        assertEquals(BookingStatus.APPROVED, booking.getStatus());
    }

    @Test
    void approveBookingBookingNotExist() {
        assertThrows(NotExistException.class, () -> bookingService.approveBooking(1L, "true", 1L));
    }

    @Test
    void approveBookingUserNotExist() {
        when(userRepository.existsById(anyLong())).thenReturn(false);

        assertThrows(NotExistException.class, () -> bookingService.approveBooking(1L, "true", 1L));
    }

    @Test
    void findByIdValid() {
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(booking));
        when(userRepository.existsById(anyLong())).thenReturn(true);

        BookingResponse response = bookingService.findById(1L, 1L);

        assertNotNull(response);
        assertEquals(booking.getId(), response.getId());
    }

    @Test
    void findByIdBookingNotExist() {
        assertThrows(NotExistException.class, () -> bookingService.findById(1L, 1L));
    }

    @Test
    void findByIdUserNotExist() {
        when(userRepository.existsById(1L)).thenReturn(false);

        assertThrows(NotExistException.class, () -> bookingService.findById(1L, 1L));
    }

    @Test
    void getBookingsByBookerValid() {
        Page<Booking> bookings = new PageImpl<>(Collections.singletonList(booking));
        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(bookingRepository.findAllByBookerIdOrderByStartDesc(anyLong(), any(PageRequest.class))).thenReturn(bookings);

        List<BookingResponse> responses = bookingService.getBookingsByBooker(1L, "ALL", 0, 10);

        assertFalse(responses.isEmpty());
        assertEquals(booking.getId(), responses.get(0).getId());
    }

    @Test
    void getBookingsByOwnerValid() {
        Page<Booking> bookings = new PageImpl<>(Collections.singletonList(booking));
        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(bookingRepository.findAllByOwnerIdOrderByStartDesc(anyLong(), any(PageRequest.class))).thenReturn(bookings);

        List<BookingResponse> responses = bookingService.getBookingsByOwner(1L, "ALL", 0, 10);

        assertFalse(responses.isEmpty());
        assertEquals(booking.getId(), responses.get(0).getId());
    }
}
