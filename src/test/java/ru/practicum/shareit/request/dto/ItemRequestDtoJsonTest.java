package ru.practicum.shareit.request.dto;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.practicum.shareit.item.dto.ItemDtoWithRequest;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@JsonTest
public class ItemRequestDtoJsonTest {

    @Autowired
    private JacksonTester<ItemRequestDto> json;

    @Test
    public void testSerialize() throws Exception {
        ItemDtoWithRequest item = ItemDtoWithRequest.builder()
                .id(1L)
                .requestId(1L)
                .name("Item")
                .description("Description")
                .available(true)
                .build();

        ItemRequestDto itemRequestDto = ItemRequestDto.builder()
                .id(1L)
                .description("test description")
                .created(LocalDateTime.of(2023, 6, 1, 12, 0))
                .build();
        itemRequestDto.getItems().add(item);

        assertThat(this.json.write(itemRequestDto))
                .hasJsonPathNumberValue("$.id")
                .extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(this.json.write(itemRequestDto))
                .hasJsonPathStringValue("$.description")
                .extractingJsonPathStringValue("$.description").isEqualTo("test description");
        assertThat(this.json.write(itemRequestDto))
                .hasJsonPathStringValue("$.created")
                .extractingJsonPathStringValue("$.created").isEqualTo("2023-06-01T12:00:00");
        assertThat(this.json.write(itemRequestDto))
                .hasJsonPathArrayValue("$.items")
                .extractingJsonPathArrayValue("$.items").hasSize(1);
    }

    @Test
    public void testDeserialize() throws Exception {
        String content = "{\"id\":1,\"description\":\"test description\"," +
                "\"created\":\"2023-06-01T12:00:00\",\"items\":[{\"id\":1,\"requestId\":1,\"name\":\"Item\"," +
                "\"description\":\"Item Description\",\"available\":true}]}";

        ItemDtoWithRequest item = ItemDtoWithRequest.builder()
                .id(1L)
                .requestId(1L)
                .name("Item")
                .description("Item Description")
                .available(true)
                .build();

        ItemRequestDto expectedItemRequestDto = ItemRequestDto.builder()
                .id(1L)
                .description("test description")
                .created(LocalDateTime.of(2023, 6, 1, 12, 0))
                .build();
        expectedItemRequestDto.getItems().add(item);

        assertThat(this.json.parse(content)).isEqualTo(expectedItemRequestDto);
        assertThat(this.json.parseObject(content).getId()).isEqualTo(1L);
        assertThat(this.json.parseObject(content).getDescription()).isEqualTo("test description");
        assertThat(this.json.parseObject(content).getCreated()).isEqualTo(LocalDateTime.of(2023, 6, 1, 12, 0));
        assertThat(this.json.parseObject(content).getItems().get(0).getId()).isEqualTo(1L);
    }
}
