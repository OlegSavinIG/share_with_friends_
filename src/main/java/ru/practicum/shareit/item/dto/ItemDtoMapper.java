package ru.practicum.shareit.item.dto;

import ru.practicum.shareit.item.model.Item;

public class ItemDtoMapper {
    static ItemDto getItemDto(Item item) {
        return ItemDto.builder()
                .title(item.getTitle())
                .description(item.getDescription())
                .review(item.getReview())
                .isAvailable(item.isAvailable())
                .build();
    }
}
