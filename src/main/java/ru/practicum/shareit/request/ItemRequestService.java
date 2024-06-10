package ru.practicum.shareit.request;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.List;

public interface ItemRequestService {
    ItemRequestDto createItemRequest(ItemRequestDto itemRequestDto, Long userId);

    List<ItemRequestDto> findItemRequestsByUser(Long userId);

    List<ItemRequestDto> findAllItemRequests(Long userId, int from, int size);

    ItemRequestDto findById(Long requestId, long userId);
}
