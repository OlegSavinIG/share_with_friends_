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
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookingServiceImplTest {

    @InjectMocks
    private BookingServiceImpl bookingService;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private UserRepository userRepository;

    private User user;
    private User owner;
    private Item item;
    private Booking booking;
    private BookingDto bookingDto;

    @BeforeEach
    public void setup() {
        user = new User();
        user.setId(1L);
        user.setName("Test User");

        owner = new User();
        owner.setId(2L);
        owner.setName("Owner");

        item = new Item();
        item.setId(1L);
        item.setName("Test Item");
        item.setUser(owner);

        bookingDto = new BookingDto();
        bookingDto.setItemId(1L);
        bookingDto.setStart(LocalDateTime.now().plusDays(1));
        bookingDto.setEnd(LocalDateTime.now().plusDays(2));

        booking = new Booking();
        booking.setId(1L);
        booking.setItem(item);
        booking.setBooker(user);
        booking.setOwner(owner);
        booking.setStart(bookingDto.getStart());
        booking.setEnd(bookingDto.getEnd());
        booking.setStatus(BookingStatus.WAITING);
    }

    @Test
    public void testCreateBookingSuccess() {
        when(userRepository.existsById(user.getId())).thenReturn(true);
        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(itemRepository.existsById(item.getId())).thenReturn(true);
        when(itemRepository.findAvailableById(item.getId())).thenReturn(true);

        BookingResponse response = bookingService.createBooking(user.getId(), bookingDto);

        assertNotNull(response);
        verify(bookingRepository, times(1)).save(any(Booking.class));
    }

    @Test
    public void testApproveBookingSuccess() {
        when(userRepository.existsById(owner.getId())).thenReturn(true);
        when(bookingRepository.findById(booking.getId())).thenReturn(Optional.of(booking));
        when(bookingRepository.existsById(booking.getId())).thenReturn(true);

        BookingResponse response = bookingService.approveBooking(booking.getId(), "true", owner.getId());

        assertNotNull(response);
        assertEquals(BookingStatus.APPROVED, booking.getStatus());
        verify(bookingRepository, times(1)).save(any(Booking.class));
    }

    @Test
    public void testFindByIdSuccess() {
        when(userRepository.existsById(user.getId())).thenReturn(true);
        when(bookingRepository.findById(booking.getId())).thenReturn(Optional.of(booking));

        BookingResponse response = bookingService.findById(booking.getId(), user.getId());

        assertNotNull(response);
        assertEquals(booking.getId(), response.getId());
    }

    @Test
    public void testGetBookingsByBookerSuccess() {
        Page<Booking> bookingsPage = new PageImpl<>(Collections.singletonList(booking));
        when(userRepository.existsById(user.getId())).thenReturn(true);
        when(bookingRepository.findAllByBookerIdOrderByStartDesc(eq(user.getId()), any(PageRequest.class)))
                .thenReturn(bookingsPage);

        List<BookingResponse> responses = bookingService.getBookingsByBooker(user.getId(), "ALL", 0, 10);

        assertFalse(responses.isEmpty());
        assertEquals(booking.getId(), responses.get(0).getId());
    }

    @Test
    public void testGetBookingsByOwnerSuccess() {
        Page<Booking> bookingsPage = new PageImpl<>(Collections.singletonList(booking));
        when(userRepository.existsById(owner.getId())).thenReturn(true);
        when(bookingRepository.findAllByOwnerIdOrderByStartDesc(eq(owner.getId()), any(PageRequest.class)))
                .thenReturn(bookingsPage);

        List<BookingResponse> responses = bookingService.getBookingsByOwner(owner.getId(), "ALL", 0, 10);

        assertFalse(responses.isEmpty());
        assertEquals(booking.getId(), responses.get(0).getId());
    }
}
