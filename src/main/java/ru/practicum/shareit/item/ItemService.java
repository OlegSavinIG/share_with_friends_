package ru.practicum.shareit.item;

import ru.practicum.shareit.item.comment.CommentDto;
import ru.practicum.shareit.item.model.ItemDto;

import java.util.List;

public interface ItemService {
    ItemDto addItem(ItemDto itemDto, Long userId);

    ItemDto getItemById(Long id, Long userId);

    ItemDto updateItem(ItemDto itemDto, long id, Long userId);

    void deleteItemById(Long id);

    List<ItemDto> getAllItemsByUserId(Long userId, int from, int size);

    List<ItemDto> searchByNameOrDescription(String text, Long userId, int from, int size);

    CommentDto createComment(Long itemId, Long userId, CommentDto commentDto);
}
