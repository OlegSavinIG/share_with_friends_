package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {
    ItemDto addItem(ItemDto itemDto, Long userId);
    ItemDto getItemById(Long id);
    ItemDto updateItem(ItemDto itemDto, long id, Long userId);
    void deleteItemById(Long id);
    List<ItemDto> getAllItemsByUserId(Long userId);
    List<ItemDto> searchByNameOrDescription(String text, Long userId);
}
