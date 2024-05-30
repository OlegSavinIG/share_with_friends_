package ru.practicum.shareit.item.memory;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class InMemoryItemStorageImpl implements ItemStorage {
    private final Map<Long, Item> items = new HashMap<>();
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
    public Item updateItem(ItemDto itemDto) {
        Item existItem = items.get(itemDto.getId());
        if (itemDto.getName() != null) {
            existItem.setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null) {
            existItem.setDescription(itemDto.getDescription());
        }
        if (itemDto.getAvailable() != null) {
            existItem.setAvailable(itemDto.getAvailable());
        }
        items.put(existItem.getId(), existItem);
        return existItem;

    }


    @Override
    public void deleteItemById(Long id) {
        items.remove(id);
    }

    @Override
    public List<Item> getAllItems(List<Long> itemIds) {
        return items.values().stream()
                .filter(item -> itemIds.contains(item.getId()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Item> searchByNameOrDescription(String text, Long userId) {
        String lowerCase = text.toLowerCase();
        return items.values().stream()
                .filter(item -> item.getAvailable())
                .filter(item -> (item.getName().toLowerCase().contains(lowerCase)
                        || item.getDescription().toLowerCase().contains(lowerCase)))
                .collect(Collectors.toList());
    }
}
