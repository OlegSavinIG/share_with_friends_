package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.annotation.Update;
import ru.practicum.shareit.exception.DataNotFoundException;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Validated
public class ItemController {
    private final ItemClient itemClient;

    @PostMapping
    public ResponseEntity<Object> create(@Valid @RequestBody ItemRequestDto itemDto,
                                          @RequestHeader(name = "X-Sharer-User-Id") Long userId) {
        if (userId == null) {
            throw new DataNotFoundException("Не передан id пользователя");
        }
        return itemClient.createItem(itemDto, userId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getById(@PathVariable Long id,
                                           @RequestHeader(name = "X-Sharer-User-Id") Long userId) {
        return itemClient.getItem(id, userId);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> update(@Validated(Update.class) @RequestBody ItemRequestDto itemDto,
                                          @PathVariable Long id,
                                          @RequestHeader(name = "X-Sharer-User-Id") Long userId) {
        if (userId == null) {
            throw new DataNotFoundException("Не передан id пользователя");
        }
        return itemClient.updateItem(itemDto, id, userId);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        return itemClient.deleteById(id);
    }

    @GetMapping
    public ResponseEntity<Object> getAll(@RequestHeader(name = "X-Sharer-User-Id", required = false) Long userId,
                                                @PositiveOrZero @RequestParam(defaultValue = "0") int from,
                                                @Positive @RequestParam(defaultValue = "10") int size) {
        return itemClient.getAllItems(userId, from , size);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchByNameOrDescription(@RequestParam String text,
                                                                   @RequestHeader(name = "X-Sharer-User-Id") Long userId,
                                                                   @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                                                   @Positive @RequestParam(defaultValue = "10") Integer size) {
        if (userId == null || text == null) {
            throw new DataNotFoundException("Не передан текст для поиска или неверный пользователь");
        }
        if (text.isBlank()) {
            return ResponseEntity.ok(Collections.emptyList());
        }
        return itemClient.searchByNameOrDescription(text, userId, from, size);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> createComment(@PathVariable Long itemId,
                                                    @RequestHeader(name = "X-Sharer-User-Id") Long userId,
                                                    @Valid @RequestBody CommentRequestDto commentDto) {
        return itemClient.createComment(itemId, userId, commentDto);
    }
}