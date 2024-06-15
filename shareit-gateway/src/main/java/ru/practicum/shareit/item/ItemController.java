package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.annotation.Update;
import ru.practicum.shareit.exception.DataNotFoundException;
import ru.practicum.shareit.item.dto.CommentRequestDto;
import ru.practicum.shareit.item.dto.ItemRequestDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.Collections;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Validated
@Slf4j
public class ItemController {
    private final ItemClient itemClient;

    @PostMapping
    public ResponseEntity<Object> create(@Valid @RequestBody ItemRequestDto itemDto,
                                         @RequestHeader(name = "X-Sharer-User-Id") Long userId) {
        if (userId == null) {
            throw new DataNotFoundException("Не передан id пользователя");
        }
        log.info("Creating item {}, userId={}", itemDto, userId);
        return itemClient.createItem(itemDto, userId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getById(@PathVariable Long id,
                                          @RequestHeader(name = "X-Sharer-User-Id") Long userId) {
        log.info("Getting item by id={}, userId={}", id, userId);
        return itemClient.getItem(id, userId);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> update(@Validated(Update.class) @RequestBody ItemRequestDto itemDto,
                                         @PathVariable Long id,
                                         @RequestHeader(name = "X-Sharer-User-Id") Long userId) {
        if (userId == null) {
            throw new DataNotFoundException("Не передан id пользователя");
        }
        log.info("Updating item id={}, itemDto={}, userId={}", id, itemDto, userId);
        return itemClient.updateItem(itemDto, id, userId);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        log.info("Deleting item by id={}", id);
        return itemClient.deleteById(id);
    }

    @GetMapping
    public ResponseEntity<Object> getAll(@RequestHeader(name = "X-Sharer-User-Id", required = false) Long userId,
                                         @PositiveOrZero @RequestParam(defaultValue = "0") int from,
                                         @Positive @RequestParam(defaultValue = "10") int size) {
        log.info("Getting all items, userId={}, from={}, size={}", userId, from, size);
        return itemClient.getAllItems(userId, from, size);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchByNameOrDescription(
            @RequestHeader(name = "X-Sharer-User-Id") Long userId,
            @RequestParam(name = "text") String text,
            @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
            @Positive @RequestParam(defaultValue = "10") Integer size) {
        if (userId == null || text == null) {
            throw new DataNotFoundException("Не передан текст для поиска или неверный пользователь");
        }
        if (text.isBlank()) {
            log.info("Search text is blank, returning empty list");
            return ResponseEntity.ok(Collections.emptyList());
        }
        log.info("Searching items by text '{}', userId={}, from={}, size={}", text, userId, from, size);
        return itemClient.searchByNameOrDescription(text, userId, from, size);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> createComment(@PathVariable Long itemId,
                                                @RequestHeader(name = "X-Sharer-User-Id") Long userId,
                                                @Valid @RequestBody CommentRequestDto commentDto) {
        log.info("Creating comment for item id={}, userId={}, comment={}", itemId, userId, commentDto);
        return itemClient.createComment(itemId, userId, commentDto);
    }
}
