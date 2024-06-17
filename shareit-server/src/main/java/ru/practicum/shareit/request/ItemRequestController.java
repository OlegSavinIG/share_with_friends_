package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.annotation.ValidFrom;
import ru.practicum.shareit.annotation.ValidSize;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import javax.validation.Valid;
import java.util.List;

/**
 * TODO Sprint add-item-requests.
 */
@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class ItemRequestController {
    private final ItemRequestService itemRequestService;

    @PostMapping
    public ItemRequestDto createRequest(@RequestBody ItemRequestDto itemRequestDto,
                                        @RequestHeader(name = "X-Sharer-User-Id") Long userId) {
        return itemRequestService.createItemRequest(itemRequestDto, userId);
    }

    @GetMapping
    public List<ItemRequestDto> findItemRequestsByUser(@RequestHeader(name = "X-Sharer-User-Id") Long userId) {
        return itemRequestService.findItemRequestsByUser(userId);
    }

    @GetMapping("/all")
    public List<ItemRequestDto> findAllItemRequests(@RequestHeader(name = "X-Sharer-User-Id") Long userId,
                                                    @RequestParam(defaultValue = "0") Integer from,
                                                    @RequestParam(defaultValue = "10") Integer size) {

        return itemRequestService.findAllItemRequests(userId, from, size);
    }

    @GetMapping("/{requestId}")
    public ItemRequestDto findById(@PathVariable Long requestId,
                                   @RequestHeader(name = "X-Sharer-User-Id") Long userId) {
        return itemRequestService.findById(requestId, userId);
    }
}
