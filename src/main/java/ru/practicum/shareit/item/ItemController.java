package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.base.BaseController;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController extends BaseController<Item, Long> {
    private final ItemService itemService;

    @Override
    protected Item createEntity(Item item) {
        return itemService.addItem(item);
    }

    @Override
    protected Item getEntityById(Long id) {
        return itemService.getItemById(id);
    }

    @Override
    protected Item updateEntity(Item item) {
        return itemService.updateItem(item);
    }

    @Override
    protected boolean deleteEntity(Long id) {
        return itemService.deleteItemById(id);
    }

    @Override
    protected List<Item> getAllEntities() {
        return itemService.getAllItems();
    }
}
