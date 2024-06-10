package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.comment.Comment;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class ItemMapperTest {

    @Autowired
    private ObjectMapper objectMapper;

    private JacksonTester<ItemDto> json;

    @Autowired
    void setUp(JacksonTester<ItemDto> json) {
        this.json = json;
    }

    @Test
    void testMapToItemDtoWithBooking() throws Exception {
        User booker = new User(1L, "User1", "user1@example.com");
        User owner = new User(2L, "User2", "user2@test.com");

        Item item = Item.builder()
                .id(1L)
                .name("Item1")
                .description("Description Item1")
                .available(true)
                .user(owner)
                .requestId(1L)
                .build();

        Booking booking = Booking.builder()
                .id(2L)
                .item(item)
                .owner(owner)
                .booker(booker)
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .status(BookingStatus.WAITING)
                .timeStatus(BookingStatus.FUTURE)
                .build();

        Comment comment = Comment.builder()
                .id(1L)
                .text("Test text")
                .createTime(LocalDateTime.now().minusDays(3))
                .author(owner)
                .build();

        item.getBookings().add(booking);
        item.getComments().add(comment);

        ItemDto itemDto = ItemMapper.mapToItemDtoWithBooking(item);

        JsonContent<ItemDto> result = json.write(itemDto);

        assertThat(result).hasJsonPathNumberValue("$.id");
        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).hasJsonPathStringValue("$.name");
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("Item1");
        assertThat(result).hasJsonPathStringValue("$.description");
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("Description Item1");
        assertThat(result).hasJsonPathBooleanValue("$.available");
        assertThat(result).extractingJsonPathBooleanValue("$.available").isEqualTo(true);

        assertThat(result).hasJsonPathMapValue("$.nextBooking");
        assertThat(result).extractingJsonPathNumberValue("$.nextBooking.id").isEqualTo(2);

        assertThat(result).hasJsonPathArrayValue("$.comments");
        assertThat(result).extractingJsonPathStringValue("$.comments[0].text").isEqualTo("Test text");
    }
}
