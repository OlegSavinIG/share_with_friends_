package ru.practicum.shareit.item.memory;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotExistException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserDtoMapper;
import ru.practicum.shareit.user.memory.inMemoryUserService;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class inMemoryItemService {

    private final ItemStorage itemStorage;
    private final inMemoryUserService userService;
    private final Map<Long, List<Long>> itemIdsForUser = new HashMap<>();

    public ItemDto addItem(ItemDto itemDto, Long userId) {
        userService.isUserExist(userId);
        UserDto userById = userService.getUserById(userId);
        Item item = ItemMapper.itemDtoToSaveItem(itemDto, UserDtoMapper.userDtoToUser(userById));
        Item itemFromStorage = itemStorage.addItem(item);
        List<Long> itemIds = itemIdsForUser.computeIfAbsent(userId, k -> new ArrayList<>());
        itemIds.add(itemFromStorage.getId());
        return ItemMapper.mapItemToItemDto(itemFromStorage);
    }

    public ItemDto getItemById(Long id) {
        Optional<Item> itemById = itemStorage.getItemById(id);
        if (itemById == null) {
            throw new NotExistException("Предмет с таким id {} не существует");
        }
        return ItemMapper.mapItemToItemDto(itemById.get());
    }

    public ItemDto updateItem(ItemDto itemDto, long id, Long userId) {
        userService.isUserExist(userId);
        if (!itemIdsForUser.containsKey(userId) || !itemIdsForUser.get(userId).contains(id)) {
            throw new NotExistException("У пользователя с id %d, нет предмета с id %d", userId, id);
        }
        itemDto.setId(id);
        Item updatedItem = itemStorage.updateItem(itemDto);
        return ItemMapper.mapItemToItemDto(updatedItem);
    }

    public void deleteItemById(Long id) {
         itemStorage.deleteItemById(id);
    }

    public List<ItemDto> getAllItems(Long userId) {
        userService.isUserExist(userId);
        List<Long> itemIds = itemIdsForUser.get(userId);
        if (itemIds.isEmpty()) {
            throw new NotExistException("У пользователя нет предметов");
        }
        List<Item> allItems = itemStorage.getAllItems(itemIds);
        List<ItemDto> itemDtos = allItems.stream().map(ItemMapper::mapItemToItemDto).collect(Collectors.toList());
        return itemDtos;
    }

    public List<ItemDto> searchByNameOrDescription(String text, Long userId) {
        userService.isUserExist(userId);
        List<Item> items = itemStorage.searchByNameOrDescription(text, userId);
        return items.stream()
                .map(ItemMapper::mapItemToItemDto)
                .collect(Collectors.toList());
    }
}
