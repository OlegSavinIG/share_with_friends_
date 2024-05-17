package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.DataNotFoundException;
import ru.practicum.shareit.exception.NotExistException;
import ru.practicum.shareit.item.comment.Comment;
import ru.practicum.shareit.item.comment.CommentDto;
import ru.practicum.shareit.item.comment.CommentMapper;
import ru.practicum.shareit.item.comment.CommentRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoWithBooking;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    @Override
    public ItemDto addItem(ItemDto itemDto, Long userId) {
        boolean userExistsById = userRepository.existsById(userId);
        if (userExistsById) {
            User user = userRepository.findById(userId).get();
            Item savedItem = itemRepository.save(ItemMapper.itemDtoToSaveItem(itemDto, user));
            return ItemMapper.mapItemToItemDto(savedItem);
        } else throw new NotExistException("Пользователь не существует с таким id %d", userId);
    }

    @Override
    public ItemDto getItemById(Long id) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new NotExistException("Предмет с этим id %d не существует", id));
        return ItemMapper.mapItemToItemDto(item);
    }

    @Override
    public ItemDto updateItem(ItemDto itemDto, long itemId, Long userId) {
        boolean userExistById = userRepository.existsById(userId);
        boolean itemExistById = itemRepository.existsById(itemId);
        if (userExistById && itemExistById) {
            Item existItem = itemRepository.findById(itemId).get();
            if (itemDto.getName() != null) {
                existItem.setName(itemDto.getName());
            }
            if (itemDto.getDescription() != null) {
                existItem.setDescription(itemDto.getDescription());
            }
            if (itemDto.getAvailable() != null) {
                existItem.setAvailable(itemDto.getAvailable());
            }
            Item savedItem = itemRepository.save(existItem);
            return ItemMapper.mapItemToItemDto(savedItem);
        } else throw new DataNotFoundException("Неправильно переданы данные для обновления");
    }

    @Override
    public void deleteItemById(Long id) {
        itemRepository.deleteById(id);
    }

    @Override
    public List<ItemDtoWithBooking> getAllItemsByUserId(Long userId) {
        List<Item> items = itemRepository.findAllByUserId(userId);
        return items.stream()
                .map(ItemMapper::mapItemToItemDtoWithBooking)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> searchByNameOrDescription(String text, Long userId) {
        boolean userExistById = userRepository.existsById(userId);
        if (userExistById) {
            List<Item> items = itemRepository.findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(text, text);
            return items.stream()
                    .map(ItemMapper::mapItemToItemDto)
                    .collect(Collectors.toList());
        } else throw new NotExistException("Пользователь не существует с таким id %d", userId);
    }

    @Override
    public CommentDto createComment(Long itemId, Long userId, CommentDto commentDto) {
        boolean userExist = userRepository.existsById(userId);
        boolean itemExist = itemRepository.existsById(itemId);
        if (!userExist) {
            throw new NotExistException("Пользователь не существует с таким id %d", userId);
        }
        if (!itemExist) {
            throw new NotExistException("Предмет не существует с таким id %d", itemId);
        }
        User user = userRepository.findById(userId).get();
        Item item = itemRepository.findById(itemId).get();
        Comment savedComment = commentRepository.save(CommentMapper.mapToComment(commentDto, user, item));
        return CommentMapper.mapToCommentDto(savedComment);
    }
}
