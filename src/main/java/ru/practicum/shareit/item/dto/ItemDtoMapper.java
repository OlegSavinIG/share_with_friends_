package ru.practicum.shareit.item.dto;

import ru.practicum.shareit.item.model.Item;

public class ItemDtoMapper {

   public static ItemDto itemToItemDto(Item item) {
        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .review(item.getReview())
                .isAvailable(item.isAvailable())
                .build();
    }
    public static Item itemDtoCreateItem(ItemDto itemDto, long userId) {
       return Item.builder()
               .name(itemDto.getName())
               .description(itemDto.getDescription())
               .isAvailable(itemDto.isAvailable())
               .ownerId(userId)
               .build();
    }
}
