package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
@Service
@RequiredArgsConstructor
public class ItemService {
    private final ItemStorage itemStorage;
    public Item addItem(Item item) {
        return itemStorage.addItem(item);
    }

    public Item getItemById(Long id) {
        return itemStorage.getItemById(id).orElseThrow(() -> new RuntimeException());
    }

    public Item updateItem(Item item) {
        return itemStorage.updateItem(item);
    }

    public boolean deleteItemById(Long id) {
        return itemStorage.deleteItemById(id);
    }

    public List<Item> getAllItems() {
        return itemStorage.getAllItems();
    }
}
