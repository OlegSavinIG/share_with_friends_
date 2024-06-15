package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.annotation.Update;
import ru.practicum.shareit.annotation.ValidFrom;
import ru.practicum.shareit.annotation.ValidSize;
import ru.practicum.shareit.exception.DataNotFoundException;
import ru.practicum.shareit.item.comment.CommentDto;
import ru.practicum.shareit.item.model.ItemDto;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Validated
public class ItemController {
    private final ItemService itemService;

    @PostMapping
    public ResponseEntity<ItemDto> create(@Valid @RequestBody ItemDto itemDto,
                                          @RequestHeader(name = "X-Sharer-User-Id", required = false) Long userId) {
        if (userId == null) {
            throw new DataNotFoundException("Не передан id пользователя");
        }
        ItemDto createdEntity = itemService.addItem(itemDto, userId);
        return ResponseEntity.ok(createdEntity);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemDto> getById(@PathVariable Long id,
                                           @RequestHeader(name = "X-Sharer-User-Id", required = false) Long userId) {
        ItemDto entity = itemService.getItemById(id, userId);
        return entity != null ? ResponseEntity.ok(entity) : ResponseEntity.notFound().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ItemDto> update(@Validated(Update.class) @RequestBody ItemDto itemDto,
                                          @PathVariable Long id,
                                          @RequestHeader(name = "X-Sharer-User-Id", required = false) Long userId) {
        if (userId == null) {
            throw new DataNotFoundException("Не передан id пользователя");
        }
        ItemDto updatedEntity = itemService.updateItem(itemDto, id, userId);
        return updatedEntity != null ? ResponseEntity.ok(updatedEntity) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        itemService.deleteItemById(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<ItemDto>> getAll(@RequestHeader(name = "X-Sharer-User-Id", required = false) Long userId,
                                                @ValidFrom @RequestParam(defaultValue = "0") int from,
                                                @ValidSize @RequestParam(defaultValue = "10") int size) {
        List<ItemDto> entities = itemService.getAllItemsByUserId(userId, from, size);
        return ResponseEntity.ok(entities);
    }

    @GetMapping("/search")
    public ResponseEntity<List<ItemDto>> searchByNameOrDescription(@RequestParam String text,
                                                                   @RequestHeader(name = "X-Sharer-User-Id") Long userId,
                                                                   @ValidFrom @RequestParam(defaultValue = "0") Integer from,
                                                                   @ValidSize @RequestParam(defaultValue = "10") Integer size) {
        if (userId == null || text == null) {
            throw new DataNotFoundException("Не передан текст для поиска или неверный пользователь");
        }
        if (text.isBlank()) {
            return ResponseEntity.ok(Collections.emptyList());
        }
        return ResponseEntity.ok(itemService.searchByNameOrDescription(text, userId, from, size));
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<CommentDto> createComment(@PathVariable Long itemId,
                                                    @RequestHeader(name = "X-Sharer-User-Id") Long userId,
                                                    @Valid @RequestBody CommentDto commentDto) {
        return ResponseEntity.ok(itemService.createComment(itemId, userId, commentDto));
    }
}
