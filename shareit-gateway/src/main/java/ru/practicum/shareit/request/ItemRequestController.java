package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.annotation.ValidFrom;
import ru.practicum.shareit.annotation.ValidSize;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Validated
@Slf4j
public class ItemRequestController {
    private final ItemRequestClient itemRequestClient;

    @PostMapping
    public ResponseEntity<Object> createRequest(@Valid @RequestBody ItemRequestDto itemRequestDto,
                                                @RequestHeader(name = "X-Sharer-User-Id") Long userId) {
        log.info("Creating item request {}, userId={}", itemRequestDto, userId);
        return itemRequestClient.createItemRequest(itemRequestDto, userId);
    }

    @GetMapping
    public ResponseEntity<Object> findItemRequestsByUser(@RequestHeader(name = "X-Sharer-User-Id") Long userId) {
        log.info("Finding item requests by userId={}", userId);
        return itemRequestClient.findItemRequestsByUser(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> findAllItemRequests(@RequestHeader(name = "X-Sharer-User-Id") Long userId,
                                                      @ValidFrom @RequestParam(defaultValue = "0") Integer from,
                                                      @ValidSize @RequestParam(defaultValue = "10") Integer size) {
        log.info("Finding all item requests, userId={}, from={}, size={}", userId, from, size);
        return itemRequestClient.findAllItemRequests(userId, from, size);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> findById(@PathVariable Long requestId,
                                           @RequestHeader(name = "X-Sharer-User-Id") Long userId) {
        log.info("Finding item request by id={}, userId={}", requestId, userId);
        return itemRequestClient.findById(requestId, userId);
    }
}
