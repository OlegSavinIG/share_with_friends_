package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.exception.DataNotFoundException;
import ru.practicum.shareit.exception.NotExistException;
import ru.practicum.shareit.item.comment.Comment;
import ru.practicum.shareit.item.comment.CommentDto;
import ru.practicum.shareit.item.comment.CommentMapper;
import ru.practicum.shareit.item.comment.CommentRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final BookingRepository bookingRepository;

    @Override
    public ItemDto addItem(ItemDto itemDto, Long userId) {
        log.info("Добавление нового предмета пользователем с id {}", userId);
        User user = getUserById(userId);
        Item savedItem = itemRepository.save(ItemMapper.itemDtoToSaveItem(itemDto, user));
        log.info("Предмет с id {} успешно добавлен", savedItem.getId());
        return ItemMapper.mapItemToItemDto(savedItem);
    }

    @Override
    public ItemDto getItemById(Long id, Long userId) {
        log.info("Запрос предмета с id {}", id);
        Item item = getItemById(id);
        boolean isOwner = bookingRepository.existsByItemIdAndOwnerId(id, userId);
        log.info("Проверка права собственности для пользователя с id {}", userId);
        return isOwner ? ItemMapper.mapToItemDtoWithBooking(item) : ItemMapper.mapToItemDtoWithoutBooking(item);
    }

    @Override
    public ItemDto updateItem(ItemDto itemDto, long itemId, Long userId) {
        log.info("Обновление предмета с id {}", itemId);
        validateUserExistence(userId);
        Item existItem = getItemById(itemId);
        updateItemFields(itemDto, existItem);
        Item savedItem = itemRepository.save(existItem);
        log.info("Предмет с id {} успешно обновлен", savedItem.getId());
        return ItemMapper.mapItemToItemDto(savedItem);
    }

    @Override
    public void deleteItemById(Long id) {
        log.info("Удаление предмета с id {}", id);
        itemRepository.deleteById(id);
        log.info("Предмет с id {} успешно удален", id);
    }

    @Override
    public List<ItemDto> getAllItemsByUserId(Long userId, int from, int size) {
        log.info("Запрос всех предметов для пользователя с id {}", userId);
        validateUserExistence(userId);
        Pageable pageable = PageRequest.of(from/size, size);
        Page<Item> items = itemRepository.findAllByUserIdOrderByBookingStartDesc(userId, pageable);
        return items.stream()
                .map(ItemMapper::mapToItemDtoWithBooking)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> searchByNameOrDescription(String nameOrDescription, Long userId, int from, int size) {
        log.info("Поиск предметов по тексту '{}'", nameOrDescription);
        validateUserExistence(userId);
        Pageable pageable = PageRequest.of(from/size, size);
        Page<Item> items = itemRepository
                .findByNameOrDescriptionContainingIgnoreCaseAndAvailable(nameOrDescription, pageable);
        return items.stream()
                .map(ItemMapper::mapItemToItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public CommentDto createComment(Long itemId, Long userId, CommentDto commentDto) {
        log.info("Создание комментария для предмета с id {} пользователем с id {}", itemId, userId);
        validateUserExistence(userId);
        validateItemExistence(itemId);
        validateCommentEligibility(itemId, userId);

        User user = getUserById(userId);
        Item item = getItemById(itemId);
        Comment savedComment = commentRepository.save(CommentMapper.mapToComment(commentDto, user, item));
        log.info("Комментарий для предмета с id {} успешно создан", itemId);
        return CommentMapper.mapToCommentDto(savedComment);
    }

    private void validateUserExistence(Long userId) {
        log.info("Проверка существования пользователя с id {}", userId);
        if (!userRepository.existsById(userId)) {
            log.info("Пользователь с id {} не существует", userId);
            throw new NotExistException(String.format("Пользователь не существует с таким id %d", userId));
        }
    }

    private void validateItemExistence(Long itemId) {
        log.info("Проверка существования предмета с id {}", itemId);
        if (!itemRepository.existsById(itemId)) {
            log.info("Предмет с id {} не существует", itemId);
            throw new NotExistException(String.format("Предмет не существует с таким id %d", itemId));
        }
    }

    private User getUserById(Long userId) {
        log.info("Получение пользователя с id {}", userId);
        return userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.info("Пользователь с id {} не найден", userId);
                    return new NotExistException(String.format("Пользователь не существует с таким id %d", userId));
                });
    }

    private Item getItemById(Long itemId) {
        log.info("Получение предмета с id {}", itemId);
        return itemRepository.findById(itemId)
                .orElseThrow(() -> {
                    log.info("Предмет с id {} не найден", itemId);
                    return new NotExistException(String.format("Предмет с этим id %d не существует", itemId));
                });
    }

    private void updateItemFields(ItemDto itemDto, Item existItem) {
        log.info("Обновление полей предмета с id {}", existItem.getId());
        if (itemDto.getName() != null) {
            log.info("Обновление имени предмета: {}", itemDto.getName());
            existItem.setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null) {
            log.info("Обновление описания предмета: {}", itemDto.getDescription());
            existItem.setDescription(itemDto.getDescription());
        }
        if (itemDto.getAvailable() != null) {
            log.info("Обновление доступности предмета: {}", itemDto.getAvailable());
            existItem.setAvailable(itemDto.getAvailable());
        }
    }

    private void validateCommentEligibility(Long itemId, Long userId) {
        log.info("Проверка возможности оставления комментария для предмета с id {} пользователем с id {}", itemId, userId);
        if (!bookingRepository.existsByItemIdAndBookerIdExcludingRejectedAndPast(itemId, userId)) {
            log.info("Пользователь с id {} не может оставить комментарий для предмета с id {}", userId, itemId);
            throw new DataNotFoundException("Вы не можете оставлять комментарии");
        }
        if (!bookingRepository.existsByBookerIdAndItemIdAndTimeStatusPastOrCurrent(userId, itemId)) {
            log.info("Пользователь с id {} не имеет права оставлять комментарии для предмета с id {}", userId, itemId);
            throw new DataNotFoundException("Вы не можете оставить комментарий");
        }
    }
}
