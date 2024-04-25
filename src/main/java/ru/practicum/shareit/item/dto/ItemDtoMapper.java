package ru.practicum.shareit.item.dto;

import ru.practicum.shareit.item.model.Item;

public class ItemDtoMapper {

   public static ItemDto itemToItemDto(Item item) {
        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .review(item.getReview())
                .available(item.getAvailable())
                .build();
    }
    public static Item itemDtoCreateItem(ItemDto itemDto, long userId) {
       return Item.builder()
               .name(itemDto.getName())
               .description(itemDto.getDescription())
               .available(itemDto.getAvailable())
               .ownerId(userId)
               .build();
    }
}
