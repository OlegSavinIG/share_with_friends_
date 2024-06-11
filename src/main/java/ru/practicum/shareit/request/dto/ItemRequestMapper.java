package ru.practicum.shareit.request.dto;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.model.ItemDtoWithRequest;
import ru.practicum.shareit.item.model.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ItemRequestMapper {
    public static ItemRequest mapToItemRequest(ItemRequestDto itemRequestDto, User user) {
        return ItemRequest.builder()
                .created(LocalDateTime.now())
                .description(itemRequestDto.getDescription())
                .user(user)
                .build();
    }

    public static ItemRequestDto mapToItemRequestDto(ItemRequest itemRequest) {
        List<Item> items = itemRequest.getItems();
        ItemRequestDto itemRequestDto = ItemRequestDto.builder()
                .id(itemRequest.getId())
                .description(itemRequest.getDescription())
                .created(itemRequest.getCreated())
                .build();
        if (!items.isEmpty()) {
            List<ItemDtoWithRequest> itemDtos = items.stream()
                    .map(ItemMapper::mapToItemDtoWithRequest)
                    .collect(Collectors.toList());
            itemRequestDto.getItems().addAll(itemDtos);
        }
        return itemRequestDto;
    }
}
