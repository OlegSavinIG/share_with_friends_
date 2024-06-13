package ru.practicum.shareit.booking.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.practicum.shareit.item.model.ItemDto;
import ru.practicum.shareit.user.model.UserDto;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@JsonTest
public class BookingResponseJsonTest {

    @Autowired
    private JacksonTester<BookingResponse> json;

    @Test
    public void testSerialize() throws Exception {
        LocalDateTime start = LocalDateTime.of(2023, 6, 1, 12, 0);
        LocalDateTime end = LocalDateTime.of(2023, 6, 2, 12, 0);
        UserDto booker = UserDto.builder().id(1L).name("Booker").email("booker@example.com").build();
        ItemDto item = ItemDto.builder().id(1L).name("Item").description("Item Description").available(true).build();
        BookingResponse bookingResponse = BookingResponse.builder()
                .id(1L)
                .start(start)
                .end(end)
                .status(BookingStatus.APPROVED)
                .booker(booker)
                .item(item)
                .build();

        assertThat(this.json.write(bookingResponse))
                .hasJsonPathNumberValue("$.id")
                .extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(this.json.write(bookingResponse))
                .hasJsonPathStringValue("$.start")
                .extractingJsonPathStringValue("$.start").isEqualTo("2023-06-01T12:00:00");
        assertThat(this.json.write(bookingResponse))
                .hasJsonPathStringValue("$.end")
                .extractingJsonPathStringValue("$.end").isEqualTo("2023-06-02T12:00:00");
        assertThat(this.json.write(bookingResponse))
                .hasJsonPathStringValue("$.status")
                .extractingJsonPathStringValue("$.status").isEqualTo("APPROVED");
        assertThat(this.json.write(bookingResponse))
                .hasJsonPathMapValue("$.booker")
                .extractingJsonPathMapValue("$.booker").containsEntry("id", 1);
        assertThat(this.json.write(bookingResponse))
                .hasJsonPathMapValue("$.booker")
                .extractingJsonPathMapValue("$.booker").containsEntry("name", "Booker");
        assertThat(this.json.write(bookingResponse))
                .hasJsonPathMapValue("$.booker")
                .extractingJsonPathMapValue("$.booker").containsEntry("email", "booker@example.com");
        assertThat(this.json.write(bookingResponse))
                .hasJsonPathMapValue("$.item")
                .extractingJsonPathMapValue("$.item").containsEntry("id", 1);
        assertThat(this.json.write(bookingResponse))
                .hasJsonPathMapValue("$.item")
                .extractingJsonPathMapValue("$.item").containsEntry("name", "Item");
        assertThat(this.json.write(bookingResponse))
                .hasJsonPathMapValue("$.item")
                .extractingJsonPathMapValue("$.item").containsEntry("description", "Item Description");
        assertThat(this.json.write(bookingResponse))
                .hasJsonPathBooleanValue("$.item.available")
                .extractingJsonPathBooleanValue("$.item.available").isTrue();
    }

    @Test
    public void testDeserialize() throws Exception {
        String content = "{\"id\":1,\"start\":\"2023-06-01T12:00:00\"," +
                "\"end\":\"2023-06-02T12:00:00\",\"status\":\"APPROVED\"," +
                "\"booker\":{\"id\":1,\"name\":\"Booker\",\"email\":\"booker@example.com\"}," +
                "\"item\":{\"id\":1,\"name\":\"Item\",\"description\":\"Item Description\",\"available\":true}}";

        BookingResponse expectedBookingResponse = BookingResponse.builder()
                .id(1L)
                .start(LocalDateTime.of(2023, 6, 1, 12, 0))
                .end(LocalDateTime.of(2023, 6, 2, 12, 0))
                .status(BookingStatus.APPROVED)
                .booker(UserDto.builder().id(1L).name("Booker").email("booker@example.com").build())
                .item(ItemDto.builder().id(1L).name("Item").description("Item Description").available(true).build())
                .build();

        assertThat(this.json.parse(content)).isEqualTo(expectedBookingResponse);
        assertThat(this.json.parseObject(content).getId()).isEqualTo(1L);
        assertThat(this.json.parseObject(content).getStart()).isEqualTo(LocalDateTime.of(2023, 6, 1, 12, 0));
        assertThat(this.json.parseObject(content).getEnd()).isEqualTo(LocalDateTime.of(2023, 6, 2, 12, 0));
        assertThat(this.json.parseObject(content).getStatus()).isEqualTo(BookingStatus.APPROVED);
        assertThat(this.json.parseObject(content).getBooker()).isEqualTo(UserDto.builder().id(1L).name("Booker").email("booker@example.com").build());
        assertThat(this.json.parseObject(content).getItem()).isEqualTo(ItemDto.builder().id(1L).name("Item").description("Item Description").available(true).build());
    }
}
