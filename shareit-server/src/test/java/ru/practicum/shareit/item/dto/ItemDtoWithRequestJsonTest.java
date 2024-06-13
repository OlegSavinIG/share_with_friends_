package ru.practicum.shareit.item.dto;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.practicum.shareit.item.model.ItemDtoWithRequest;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@JsonTest
public class ItemDtoWithRequestJsonTest {

    @Autowired
    private JacksonTester<ItemDtoWithRequest> json;

    @Test
    public void testSerialize() throws Exception {
        ItemDtoWithRequest itemDtoWithRequest = ItemDtoWithRequest.builder()
                .id(1L)
                .name("Item Name")
                .description("Item Description")
                .available(true)
                .requestId(2L)
                .build();

        assertThat(this.json.write(itemDtoWithRequest))
                .hasJsonPathNumberValue("$.id")
                .extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(this.json.write(itemDtoWithRequest))
                .hasJsonPathStringValue("$.name")
                .extractingJsonPathStringValue("$.name").isEqualTo("Item Name");
        assertThat(this.json.write(itemDtoWithRequest))
                .hasJsonPathStringValue("$.description")
                .extractingJsonPathStringValue("$.description").isEqualTo("Item Description");
        assertThat(this.json.write(itemDtoWithRequest))
                .hasJsonPathBooleanValue("$.available")
                .extractingJsonPathBooleanValue("$.available").isEqualTo(true);
        assertThat(this.json.write(itemDtoWithRequest))
                .hasJsonPathNumberValue("$.requestId")
                .extractingJsonPathNumberValue("$.requestId").isEqualTo(2);
    }

    @Test
    public void testDeserialize() throws Exception {
        String content = "{\"id\":1,\"name\":\"Item Name\"," +
                "\"description\":\"Item Description\",\"available\":true,\"requestId\":2}";

        ItemDtoWithRequest itemDtoWithRequest = this.json.parse(content).getObject();

        assertThat(itemDtoWithRequest.getId()).isEqualTo(1L);
        assertThat(itemDtoWithRequest.getName()).isEqualTo("Item Name");
        assertThat(itemDtoWithRequest.getDescription()).isEqualTo("Item Description");
        assertThat(itemDtoWithRequest.getAvailable()).isEqualTo(true);
        assertThat(itemDtoWithRequest.getRequestId()).isEqualTo(2L);
    }
}
