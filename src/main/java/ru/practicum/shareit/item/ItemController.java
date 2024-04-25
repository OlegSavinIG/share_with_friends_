package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.base.BaseController;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController extends BaseController<ItemDto, Long> {
    private final ItemService itemService;

    @Override
    protected ItemDto createEntity(ItemDto itemDto, Long userId) {
        return itemService.addItem(itemDto, userId);
    }

    @Override
    protected ItemDto getEntityById(Long id) {
        return itemService.getItemById(id);
    }

    @Override
    protected ItemDto updateEntity(ItemDto itemDto, Long id, Long userId) {
        return itemService.updateItem(itemDto, id, userId);
    }

    @Override
    protected boolean deleteEntity(Long id) {
        return itemService.deleteItemById(id);
    }

    @Override
    protected List<ItemDto> getAllEntities() {
        return itemService.getAllItems();
    }
}
