package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingResponse;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
public class BookingServiceImplIntegrationTest {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private UserRepository userRepository;

    private User booker;
    private User owner;
    private Item item;

    @BeforeEach
    public void setup() {
        userRepository.deleteAll();
        itemRepository.deleteAll();
        bookingRepository.deleteAll();

        owner = new User();
        owner.setName("Owner");
        owner.setEmail("owner@example.com");
        owner = userRepository.save(owner);

        booker = new User();
        booker.setName("Booker");
        booker.setEmail("booker@example.com");
        booker = userRepository.save(booker);

        item = new Item();
        item.setName("Item");
        item.setDescription("Description");
        item.setAvailable(true);
        item.setUser(owner);
        item = itemRepository.save(item);

        Booking booking1 = Booking.builder()
                .item(item)
                .booker(booker)
                .owner(owner)
                .status(BookingStatus.APPROVED)
                .start(LocalDateTime.now().minusDays(1))
                .end(LocalDateTime.now().plusDays(1))
                .build();

        Booking booking2 = Booking.builder()
                .item(item)
                .booker(booker)
                .owner(owner)
                .status(BookingStatus.APPROVED)
                .start(LocalDateTime.now().minusDays(2))
                .end(LocalDateTime.now().minusDays(1))
                .build();

        bookingRepository.save(booking1);
        bookingRepository.save(booking2);
    }

    @Test
    public void testGetBookingsByBooker() {
        List<BookingResponse> bookings = bookingService.getBookingsByBooker(booker.getId(), "ALL", 0, 10);

        assertFalse(bookings.isEmpty());
        assertEquals(2, bookings.size());
        assertEquals(booker.getId(), bookings.get(0).getBooker().getId());
        assertEquals(booker.getId(), bookings.get(1).getBooker().getId());
    }

    @Test
    public void testAllBookingsByOwner() {
        List<BookingResponse> bookings = bookingService.allBookingsByOwner(owner.getId(), 0, 10);

        assertFalse(bookings.isEmpty());
        assertEquals(2, bookings.size());
    }

    @Test
    public void testAllBookingsByBooker() {
        List<BookingResponse> bookings = bookingService.allBookingsByBooker(booker.getId(), 0, 10);

        assertFalse(bookings.isEmpty());
        assertEquals(2, bookings.size());
    }

    @Test
    public void testGetBookingsByOwnerWithState() {
        List<BookingResponse> bookings = bookingService.getBookingsByOwner(owner.getId(), "CURRENT", 0, 10);

        assertFalse(bookings.isEmpty());
        assertEquals(1, bookings.size());
    }
}
