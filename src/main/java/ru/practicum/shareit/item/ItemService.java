package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotExistException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserService;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemStorage itemStorage;
    private final UserService userService;
    private Map<Long, List<Long>> itemIdsForUser = new HashMap<>();

    public ItemDto addItem(ItemDto itemDto, Long userId) {
//        if (!userService.isUserExist(userId)) {
//            throw new NotExistException("Пользователь не существует");
//        }
        userService.isUserExist(userId);
        Item item = ItemDtoMapper.itemDtoCreateItem(itemDto, userId);
        Item itemFromStorage = itemStorage.addItem(item);
        List<Long> itemIds = itemIdsForUser.computeIfAbsent(userId, k -> new ArrayList<>());
        itemIds.add(itemFromStorage.getId());
        return ItemDtoMapper.itemToItemDto(itemFromStorage);
    }

    public ItemDto getItemById(Long id) {
        Optional<Item> itemById = itemStorage.getItemById(id);
        if (itemById == null) {
            throw new NotExistException("Предмет с таким id {} не существует");
        }
        return ItemDtoMapper.itemToItemDto(itemById.get());
    }

    public ItemDto updateItem(ItemDto itemDto, long id, Long userId) {
//        if (!userService.isUserExist(userId)) {
//            throw new NotExistException("Пользователь не существует");
//        }
        userService.isUserExist(userId);
        if (!itemIdsForUser.keySet().contains(userId) || !itemIdsForUser.get(userId).contains(id)) {
            throw new NotExistException("У пользователя с id %d, нет предмета с id %d", userId, id);
        }
        itemDto.setId(id);
        Item updatedItem = itemStorage.updateItem(itemDto);
        return ItemDtoMapper.itemToItemDto(updatedItem);
    }

    public boolean deleteItemById(Long id) {
        return itemStorage.deleteItemById(id);
    }

    public List<ItemDto> getAllItems(Long userId) {
//        if (!userService.isUserExist(userId)) {
//            throw new NotExistException("Пользователь не существует");
//        }
        userService.isUserExist(userId);
        List<Long> itemIds = itemIdsForUser.get(userId);
        if (itemIds.isEmpty()) {
            throw new NotExistException("У пользователя нет предметов");
        }
        List<Item> allItems = itemStorage.getAllItems(itemIds);
        List<ItemDto> itemDtos = allItems.stream().map(ItemDtoMapper::itemToItemDto).collect(Collectors.toList());
        return itemDtos;
    }

    public List<ItemDto> searchByNameOrDescription(String text, Long userId) {
        userService.isUserExist(userId);
        List<Item> items = itemStorage.searchByNameOrDescription(text, userId);
        return items.stream()
                .map(ItemDtoMapper::itemToItemDto)
                .collect(Collectors.toList());
    }
}
