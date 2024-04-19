package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

public interface ItemStorage {
    Item addItem(ItemDto item);

    Optional<Item> getItemById(Long id);

    Item updateItem(ItemDto item);

    boolean deleteItemById(Long id);

    List<Item> getAllItems();
}
