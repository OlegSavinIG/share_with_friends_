package ru.practicum.shareit.item;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;
public interface ItemStorage {
    Item addItem(Item item);

    Optional<Item> getItemById(Long id);

    Item updateItem(ItemDto item, long id);

    boolean deleteItemById(Long id);

    List<Item> getAllItems();
}
