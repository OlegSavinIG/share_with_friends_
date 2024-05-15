package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.base.BaseController;
import ru.practicum.shareit.exception.DataNotFoundException;
import ru.practicum.shareit.item.comment.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoWithBooking;

import java.util.Collections;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController extends BaseController<ItemDto, Long, ItemDtoWithBooking> {
    private final ItemService itemService;

    @Override
    protected ItemDto createEntity(ItemDto itemDto, Long userId) {
        if (userId == null) {
            throw new DataNotFoundException("Не передан id пользователя");
        }
        return itemService.addItem(itemDto, userId);
    }

    @Override
    protected ItemDto getEntityById(Long id) {
        return itemService.getItemById(id);
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
    protected List<ItemDtoWithBooking> getAllEntities(Long userId) {
        return itemService.getAllItemsByUserId(userId);
    }


    @GetMapping("/search")
    public List<ItemDto> searchByNameOrDescription(@RequestParam String text,
                                                   @RequestHeader(name = "X-Sharer-User-Id") Long userId) {
        if (userId == null || text == null) {
            throw new DataNotFoundException("Не передан текст для поиска или неверный пользователь");
        }
        if (text.isBlank()) {
            return Collections.emptyList();
        }
        return itemService.searchByNameOrDescription(text, userId);
    }
    @PostMapping("/{itemId}/comment")
    private CommentDto createComment(@PathVariable Long itemId,
                                     @RequestHeader(name = "X-Sharer-User-Id") Long userId,
                                     @RequestBody CommentDto commentDto) {
        return itemService.createComment(itemId, userId, commentDto);
    }
}
