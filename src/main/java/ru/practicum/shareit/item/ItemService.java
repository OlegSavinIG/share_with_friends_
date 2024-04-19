package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemService {
    private final ItemStorage itemStorage;
    private final UserService userService;

    public ItemDto addItem(ItemDto item, Long userId) {
        if (!userService.isUserExist(userId)) {
            throw new RuntimeException();
        }
        Item itemFromStorage = itemStorage.addItem(item);
        return ItemDtoMapper.itemToItemDto(itemFromStorage);
    }

    public ItemDto getItemById(Long id) {
        Optional<Item> itemById = itemStorage.getItemById(id);
        if (itemById == null) {
            throw new RuntimeException();
        }
        return ItemDtoMapper.itemToItemDto(itemById.get());
    }

    public ItemDto updateItem(ItemDto item, Long userId) {
        itemStorage.updateItem(item);
    }

    public boolean deleteItemById(Long id) {
        return itemStorage.deleteItemById(id);
    }

    public List<ItemDto> getAllItems() {
        List<Item> allItems = itemStorage.getAllItems();
        List<ItemDto> itemDtos = allItems.stream().map(ItemDtoMapper::itemToItemDto).collect(Collectors.toList());
        return itemDtos;
    }
}
