package ru.practicum.shareit.request.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import ru.practicum.shareit.item.dto.ItemDtoWithRequest;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@JsonTest
public class ItemRequestMapperTest {

    @Autowired
    private ObjectMapper objectMapper;

    private User user;
    private ItemRequest itemRequest;
    private ItemRequestDto itemRequestDto;
    private Item item;
    private ItemDtoWithRequest itemDtoWithRequest;

    @BeforeEach
    public void setup() {
        user = User.builder()
                .id(1L)
                .name("User")
                .email("user@example.com")
                .build();

        item = Item.builder()
                .id(1L)
                .name("Item")
                .description("Item Description")
                .available(true)
                .user(user)
                .requestId(1L)
                .build();

        itemDtoWithRequest = ItemDtoWithRequest.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .requestId(item.getRequestId())
                .build();

        itemRequest = ItemRequest.builder()
                .id(1L)
                .description("Request Description")
                .created(LocalDateTime.now())
                .user(user)
                .build();
        itemRequest.getItems().add(item);

        itemRequestDto = ItemRequestDto.builder()
                .id(itemRequest.getId())
                .description(itemRequest.getDescription())
                .created(itemRequest.getCreated())
                .build();
        itemRequestDto.getItems().add(itemDtoWithRequest);
    }

    @Test
    public void testMapToItemRequest() throws Exception {
        ItemRequest mappedItemRequest = ItemRequestMapper.mapToItemRequest(itemRequestDto, user);

        assertEquals(itemRequestDto.getDescription(), mappedItemRequest.getDescription());
        assertEquals(user.getId(), mappedItemRequest.getUser().getId());
//        assertEquals(itemRequestDto.getCreated().withNano(0), mappedItemRequest.getCreated().withNano(0));
    }

    @Test
    public void testMapToItemRequestDto() throws Exception {
        ItemRequestDto mappedItemRequestDto = ItemRequestMapper.mapToItemRequestDto(itemRequest);

        assertEquals(itemRequest.getId(), mappedItemRequestDto.getId());
        assertEquals(itemRequest.getDescription(), mappedItemRequestDto.getDescription());
//        assertEquals(itemRequest.getCreated().withNano(0), mappedItemRequestDto.getCreated().withNano(0));
        assertEquals(item.getId(), mappedItemRequestDto.getItems().get(0).getId());
    }
}
