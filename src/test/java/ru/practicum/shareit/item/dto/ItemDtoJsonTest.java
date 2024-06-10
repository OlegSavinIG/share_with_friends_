package ru.practicum.shareit.item.dto;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.boot.test.json.JacksonTester;
import ru.practicum.shareit.booking.model.BookingResponseWithItem;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.comment.CommentDto;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@JsonTest
public class ItemDtoJsonTest {

    @Autowired
    private JacksonTester<ItemDto> json;

    @Test
    public void testSerialize() throws Exception {
        BookingResponseWithItem lastBooking = BookingResponseWithItem.builder()
                .id(1L)
                .bookerId(1L)
                .start(LocalDateTime.of(2023, 6, 1, 12, 0))
                .end(LocalDateTime.of(2023, 6, 2, 12, 0))
                .status(BookingStatus.APPROVED)
                .build();

        BookingResponseWithItem nextBooking = BookingResponseWithItem.builder()
                .id(2L)
                .bookerId(2L)
                .start(LocalDateTime.of(2023, 7, 1, 12, 0))
                .end(LocalDateTime.of(2023, 7, 2, 12, 0))
                .status(BookingStatus.APPROVED)
                .build();

        CommentDto comment = CommentDto.builder()
                .id(1L)
                .text("Test Comment")
                .authorName("Author")
                .created(LocalDateTime.of(2023, 6, 3, 12, 0))
                .build();

        ItemDto itemDto = ItemDto.builder()
                .id(1L)
                .name("Item")
                .description("Item Description")
                .available(true)
                .requestId(1L)
                .lastBooking(lastBooking)
                .nextBooking(nextBooking)
                .comments(List.of(comment))
                .build();

        assertThat(this.json.write(itemDto))
                .hasJsonPathNumberValue("$.id")
                .extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(this.json.write(itemDto))
                .hasJsonPathStringValue("$.name")
                .extractingJsonPathStringValue("$.name").isEqualTo("Item");
        assertThat(this.json.write(itemDto))
                .hasJsonPathBooleanValue("$.available")
                .extractingJsonPathBooleanValue("$.available").isTrue();
        assertThat(this.json.write(itemDto))
                .hasJsonPathStringValue("$.description")
                .extractingJsonPathStringValue("$.description").isEqualTo("Item Description");
        assertThat(this.json.write(itemDto))
                .hasJsonPathNumberValue("$.requestId")
                .extractingJsonPathNumberValue("$.requestId").isEqualTo(1);
        assertThat(this.json.write(itemDto))
                .hasJsonPathMapValue("$.lastBooking")
                .extractingJsonPathMapValue("$.lastBooking").containsEntry("id", 1);
        assertThat(this.json.write(itemDto))
                .hasJsonPathMapValue("$.nextBooking")
                .extractingJsonPathMapValue("$.nextBooking").containsEntry("id", 2);
        assertThat(this.json.write(itemDto))
                .hasJsonPathArrayValue("$.comments")
                .extractingJsonPathArrayValue("$.comments").hasSize(1);
    }

    @Test
    public void testDeserialize() throws Exception {
        String content = "{\"id\":1,\"name\":\"Item\",\"description\":\"Item Description\",\"available\":true,\"requestId\":1,\"lastBooking\":{\"id\":1,\"bookerId\":1,\"start\":\"2023-06-01T12:00:00\",\"end\":\"2023-06-02T12:00:00\",\"status\":\"APPROVED\"},\"nextBooking\":{\"id\":2,\"bookerId\":2,\"start\":\"2023-07-01T12:00:00\",\"end\":\"2023-07-02T12:00:00\",\"status\":\"APPROVED\"},\"comments\":[{\"id\":1,\"text\":\"Test Comment\",\"authorName\":\"Author\",\"created\":\"2023-06-03T12:00:00\"}]}";

        ItemDto expectedItemDto = ItemDto.builder()
                .id(1L)
                .name("Item")
                .description("Item Description")
                .available(true)
                .requestId(1L)
                .lastBooking(BookingResponseWithItem.builder()
                        .id(1L)
                        .bookerId(1L)
                        .start(LocalDateTime.of(2023, 6, 1, 12, 0))
                        .end(LocalDateTime.of(2023, 6, 2, 12, 0))
                        .status(BookingStatus.APPROVED)
                        .build())
                .nextBooking(BookingResponseWithItem.builder()
                        .id(2L)
                        .bookerId(2L)
                        .start(LocalDateTime.of(2023, 7, 1, 12, 0))
                        .end(LocalDateTime.of(2023, 7, 2, 12, 0))
                        .status(BookingStatus.APPROVED)
                        .build())
                .comments(List.of(CommentDto.builder()
                        .id(1L)
                        .text("Test Comment")
                        .authorName("Author")
                        .created(LocalDateTime.of(2023, 6, 3, 12, 0))
                        .build()))
                .build();

        assertThat(this.json.parse(content)).isEqualTo(expectedItemDto);
        assertThat(this.json.parseObject(content).getId()).isEqualTo(1L);
        assertThat(this.json.parseObject(content).getName()).isEqualTo("Item");
        assertThat(this.json.parseObject(content).getDescription()).isEqualTo("Item Description");
        assertThat(this.json.parseObject(content).getAvailable()).isTrue();
        assertThat(this.json.parseObject(content).getRequestId()).isEqualTo(1L);
        assertThat(this.json.parseObject(content).getLastBooking().getId()).isEqualTo(1L);
        assertThat(this.json.parseObject(content).getNextBooking().getId()).isEqualTo(2L);
        assertThat(this.json.parseObject(content).getComments().get(0).getId()).isEqualTo(1L);
    }
}
