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
public class BookingResponseWithItemJsonTest {

    @Autowired
    private JacksonTester<BookingResponseWithItem> json;

    @Test
    public void testSerialize() throws Exception {
        BookingResponseWithItem bookingResponseWithItem = BookingResponseWithItem.builder()
                .id(1L)
                .start(LocalDateTime.of(2023, 6, 1, 12, 0))
                .end(LocalDateTime.of(2023, 6, 2, 12, 0))
                .status(BookingStatus.APPROVED)
                .bookerId(1L)
                .build();

        assertThat(this.json.write(bookingResponseWithItem))
                .hasJsonPathNumberValue("$.id")
                .extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(this.json.write(bookingResponseWithItem))
                .hasJsonPathStringValue("$.start")
                .extractingJsonPathStringValue("$.start").isEqualTo("2023-06-01T12:00:00");
        assertThat(this.json.write(bookingResponseWithItem))
                .hasJsonPathStringValue("$.end")
                .extractingJsonPathStringValue("$.end").isEqualTo("2023-06-02T12:00:00");
        assertThat(this.json.write(bookingResponseWithItem))
                .hasJsonPathStringValue("$.status")
                .extractingJsonPathStringValue("$.status").isEqualTo("APPROVED");
        assertThat(this.json.write(bookingResponseWithItem))
                .hasJsonPathNumberValue("$.bookerId")
                .extractingJsonPathNumberValue("$.bookerId").isEqualTo(1);
    }

    @Test
    public void testDeserialize() throws Exception {
        String content = "{\"id\":1,\"start\":\"2023-06-01T12:00:00\"" +
                ",\"end\":\"2023-06-02T12:00:00\",\"status\":\"APPROVED\",\"bookerId\":1}";

        BookingResponseWithItem bookingResponseWithItem = this.json.parse(content).getObject();

        assertThat(bookingResponseWithItem.getId()).isEqualTo(1L);
        assertThat(bookingResponseWithItem.getStart()).isEqualTo(LocalDateTime.of(2023, 6, 1, 12, 0));
        assertThat(bookingResponseWithItem.getEnd()).isEqualTo(LocalDateTime.of(2023, 6, 2, 12, 0));
        assertThat(bookingResponseWithItem.getStatus()).isEqualTo(BookingStatus.APPROVED);
        assertThat(bookingResponseWithItem.getBookerId()).isEqualTo(1L);
    }
}
