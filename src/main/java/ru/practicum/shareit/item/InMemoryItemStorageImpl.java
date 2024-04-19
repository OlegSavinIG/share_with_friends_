package ru.practicum.shareit.item;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

public class InMemoryItemStorageImpl implements ItemStorage {
    private Map<Long, Item> items = new HashMap<>();
    private long generatedId = 1;
    @Override
    public Item addItem(ItemDto item) {
        item.setId(generatedId++);
        return items.put(item.getId(), item);
    }

    @Override
    public Optional<Item> getItemById(Long id) {
        return Optional.ofNullable(items.get(id));
    }

    @Override
    public Item updateItem(ItemDto item)  {
        Item existItem = items.get(item.getId());
        Field[] declaredFields = item.getClass().getDeclaredFields();
        Arrays.stream(declaredFields).forEach(field -> field.setAccessible(true));
        for (Field declaredField : declaredFields) {
            Object value = declaredField.get(item);
            if (value != null) {
                Field itemField = existItem.getClass().getDeclaredField(declaredField.getName());
                itemField.setAccessible(true);
                itemField.set(existItem, value);
            }
        }
        return items.put(existItem.getId(), existItem);
    }

    @Override
    public boolean deleteItemById(Long id) {
        return items.remove(id) != null;
    }

    @Override
    public List<Item> getAllItems() {
        return (List) items.values();
    }
}
