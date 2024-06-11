package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.BaseController;
import ru.practicum.shareit.exception.DataNotFoundException;
import ru.practicum.shareit.item.comment.CommentDto;
import ru.practicum.shareit.item.model.ItemDto;

import javax.validation.Valid;
import java.util.Collections;
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
        if (userId == null) {
            throw new DataNotFoundException("Не передан id пользователя");
        }
        return itemService.addItem(itemDto, userId);
    }

    @Override
    protected ItemDto getEntityById(Long id, Long userId) {
        return itemService.getItemById(id, userId);
    }

    @Override
    protected ItemDto updateEntity(ItemDto itemDto, Long itemId, Long userId) {
        if (userId == null) {
            throw new DataNotFoundException("Не передан id пользователя");
        }
        return itemService.updateItem(itemDto, itemId, userId);
    }

    @Override
    protected void deleteEntity(Long id) {
        itemService.deleteItemById(id);
    }

    @Override
    protected List<ItemDto> getAllEntities(Long userId, int from, int size) {
        if (from < 0) {
            throw new IllegalArgumentException("Параметр 'from' не может быть отрицательным ");
        }
        if (size <= 0) {
            throw new IllegalArgumentException("Параметр 'size' должен быть больше нуля");
        }
        return itemService.getAllItemsByUserId(userId, from, size);
    }


    @GetMapping("/search")
    public List<ItemDto> searchByNameOrDescription(@RequestParam String text,
                                                   @RequestHeader(name = "X-Sharer-User-Id") Long userId,
                                                   @RequestParam(defaultValue = "0") Integer from,
                                                   @RequestParam(defaultValue = "10") Integer size) {

        if (from < 0) {
            throw new IllegalArgumentException("Параметр 'from' не может быть отрицательным ");
        }
        if (size <= 0) {
            throw new IllegalArgumentException("Параметр 'size' должен быть больше нуля");
        }
        if (userId == null || text == null) {
            throw new DataNotFoundException("Не передан текст для поиска или неверный пользователь");
        }
        if (text.isBlank()) {
            return Collections.emptyList();
        }
        return itemService.searchByNameOrDescription(text, userId, from, size);
    }

    @PostMapping("/{itemId}/comment")
    private CommentDto createComment(@PathVariable Long itemId,
                                     @RequestHeader(name = "X-Sharer-User-Id") Long userId,
                                     @Valid @RequestBody CommentDto commentDto) {
        return itemService.createComment(itemId, userId, commentDto);
    }
}
