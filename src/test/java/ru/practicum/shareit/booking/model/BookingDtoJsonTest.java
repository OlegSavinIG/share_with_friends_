package ru.practicum.shareit.booking.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@JsonTest
public class BookingDtoJsonTest {

    @Autowired
    private JacksonTester<BookingDto> json;

    @Test
    public void testSerialize() throws Exception {
        LocalDateTime start = LocalDateTime.of(2023, 6, 1, 12, 0);
        LocalDateTime end = LocalDateTime.of(2023, 6, 2, 12, 0);
        BookingDto bookingDto = BookingDto.builder()
                .id(1L)
                .itemId(2L)
                .start(start)
                .end(end)
                .status(BookingStatus.APPROVED)
                .build();

        String expectedJson = "{\"id\":1,\"itemId\":2,\"start\":\"2023-06-01T12:00:00\",\"end\":\"2023-06-02T12:00:00\",\"status\":\"APPROVED\"}";

        assertThat(this.json.write(bookingDto)).isEqualToJson(expectedJson);
    }

    @Test
    public void testDeserialize() throws Exception {
        String content = "{\"id\":1,\"itemId\":2,\"start\":\"2023-06-01T12:00:00\",\"end\":\"2023-06-02T12:00:00\",\"status\":\"APPROVED\"}";

        BookingDto expectedBookingDto = BookingDto.builder()
                .id(1L)
                .itemId(2L)
                .start(LocalDateTime.of(2023, 6, 1, 12, 0))
                .end(LocalDateTime.of(2023, 6, 2, 12, 0))
                .status(BookingStatus.APPROVED)
                .build();

        assertThat(this.json.parse(content)).isEqualTo(expectedBookingDto);
        assertThat(this.json.parseObject(content).getId()).isEqualTo(1L);
        assertThat(this.json.parseObject(content).getItemId()).isEqualTo(2L);
        assertThat(this.json.parseObject(content).getStart()).isEqualTo(LocalDateTime.of(2023, 6, 1, 12, 0));
        assertThat(this.json.parseObject(content).getEnd()).isEqualTo(LocalDateTime.of(2023, 6, 2, 12, 0));
        assertThat(this.json.parseObject(content).getStatus()).isEqualTo(BookingStatus.APPROVED);
    }
}
