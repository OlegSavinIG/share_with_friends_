package ru.practicum.shareit.item;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class InMemoryItemStorageImpl implements ItemStorage {
    private Map<Long, Item> items = new HashMap<>();
    private long generatedId = 1;
    @Override
    public Item addItem(Item item) {
        item.setId(generatedId++);
        return items.put(item.getId(), item);
    }

    @Override
    public Optional<Item> getItemById(Long id) {
        return Optional.ofNullable(items.get(id));
    }

    @Override
    public Item updateItem(Item item) {
        items.put(item.getId(), item);
        return item;
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
