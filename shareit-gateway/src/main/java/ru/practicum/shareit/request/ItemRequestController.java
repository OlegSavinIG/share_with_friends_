package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.annotation.ValidFrom;
import ru.practicum.shareit.annotation.ValidSize;

import javax.validation.Valid;
import java.util.List;

/**
 * TODO Sprint add-item-requests.
 */
@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Validated
public class ItemRequestController {
    private final ItemRequestClient itemRequestClient;

    @PostMapping
    public ResponseEntity<Object> createRequest(@Valid @RequestBody ItemRequestDto itemRequestDto,
                                                @RequestHeader(name = "X-Sharer-User-Id") Long userId) {
        return itemRequestClient.createItemRequest(itemRequestDto, userId);
    }

    @GetMapping
    public ResponseEntity<Object> findItemRequestsByUser(@RequestHeader(name = "X-Sharer-User-Id") Long userId) {
        return itemRequestClient.findItemRequestsByUser(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> findAllItemRequests(@RequestHeader(name = "X-Sharer-User-Id") Long userId,
                                                   @ValidFrom @RequestParam(defaultValue = "0") Integer from,
                                                   @ValidSize @RequestParam(defaultValue = "10") Integer size) {

        return itemRequestClient.findAllItemRequests(userId, from, size);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> findById(@PathVariable Long requestId,
                                   @RequestHeader(name = "X-Sharer-User-Id") Long userId) {
        return itemRequestClient.findById(requestId, userId);
    }
}
