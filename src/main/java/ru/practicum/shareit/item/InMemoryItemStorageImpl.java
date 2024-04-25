package ru.practicum.shareit.item;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;
@Repository
public class InMemoryItemStorageImpl implements ItemStorage {
    private Map<Long, Item> items = new HashMap<>();
    private long generatedId = 1;
    @Override
    public Item addItem(Item item) {
        item.setId(generatedId++);
        items.put(item.getId(), item);
        return item;
    }

    @Override
    public Optional<Item> getItemById(Long id) {
        return Optional.ofNullable(items.get(id));
    }

    @Override
    public Item updateItem(ItemDto item, long id) {
        try {
            Item existItem = items.get(id);
            Field[] declaredFields = item.getClass().getDeclaredFields();
            for (Field field : declaredFields) {
                field.setAccessible(true);
                Object value = field.get(item);
                if (value != null) {
                    Field itemField = existItem.getClass().getDeclaredField(field.getName());
                    itemField.setAccessible(true);
                    itemField.set(existItem, value);
                }
            }
            items.put(existItem.getId(), existItem);
            return existItem;
        } catch (IllegalAccessException | NoSuchFieldException | SecurityException e) {
            System.err.println("Ошибка при обновлении элемента: " + e.getMessage());
            return null;
        }
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
