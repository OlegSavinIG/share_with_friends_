package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class BookingRepositoryTest {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRepository itemRepository;

    private User owner;
    private User booker;
    private Item item;
    private Booking booking;

    @BeforeEach
    void setUp() {
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
        item.setDescription("Item Description");
        item.setAvailable(true);
        item.setUser(owner);
        item = itemRepository.save(item);

        booking = new Booking();
        booking.setItem(item);
        booking.setBooker(booker);
        booking.setOwner(owner);
        booking.setStatus(BookingStatus.WAITING);
        booking.setStart(LocalDateTime.now().plusDays(1));
        booking.setEnd(LocalDateTime.now().plusDays(2));
        booking = bookingRepository.save(booking);
    }

    @Test
    void testExistsByItemIdAndOwnerId() {
        boolean exists = bookingRepository.existsByItemIdAndOwnerId(item.getId(), owner.getId());
        assertThat(exists).isTrue();
    }

    @Test
    void testExistsByItemIdAndBookerIdExcludingRejectedAndPast() {
        boolean exists = bookingRepository.existsByItemIdAndBookerIdExcludingRejectedAndPast(item.getId(), booker.getId());
        assertThat(exists).isTrue();
    }

    @Test
    void testExistsByBookerIdAndItemIdAndTimeStatusPastOrCurrent() {
        boolean exists = bookingRepository.existsByBookerIdAndItemIdAndTimeStatusPastOrCurrent(booker.getId(), item.getId());
        assertThat(exists).isFalse();
    }

    @Test
    void testFindAllByBookerIdOrderByStartDesc() {
        Page<Booking> bookings = bookingRepository.findAllByBookerIdOrderByStartDesc(booker.getId(), PageRequest.of(0, 10));
        assertThat(bookings.getTotalElements()).isEqualTo(1);
        assertThat(bookings.getContent().get(0).getId()).isEqualTo(booking.getId());
    }

    @Test
    void testFindAllByOwnerIdOrderByStartDesc() {
        Page<Booking> bookings = bookingRepository.findAllByOwnerIdOrderByStartDesc(owner.getId(), PageRequest.of(0, 10));
        assertThat(bookings.getTotalElements()).isEqualTo(1);
        assertThat(bookings.getContent().get(0).getId()).isEqualTo(booking.getId());
    }
}
