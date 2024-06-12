package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotExistException;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestMapper;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ItemRequestServiceImpl implements ItemRequestService {
    private final ItemRequestRepository repository;
    private final UserRepository userRepository;

    @Override
    public ItemRequestDto createItemRequest(ItemRequestDto itemRequestDto, Long userId) {
        isUserExists(userId);
        User user = userRepository.findById(userId).get();
        ItemRequest savedItemRequest = repository.save(ItemRequestMapper.mapToItemRequest(itemRequestDto, user));
        return ItemRequestMapper.mapToItemRequestDto(savedItemRequest);
    }

    @Override
    public List<ItemRequestDto> findItemRequestsByUser(Long userId) {
        isUserExists(userId);
        List<ItemRequest> requests = repository.findByUserId(userId);
        return requests.stream()
                .map(ItemRequestMapper::mapToItemRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemRequestDto> findAllItemRequests(Long userId, int from, int size) {
        isUserExists(userId);
        Pageable pageable = PageRequest.of(from / size, size);
        Page<ItemRequest> itemRequestPage = repository.findAllExcludingUserId(userId, pageable);
        return itemRequestPage.stream()
                .map(ItemRequestMapper::mapToItemRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    public ItemRequestDto findById(Long requestId, long userId) {
        isUserExists(userId);
        ItemRequest itemRequest = repository.findById(requestId)
                .orElseThrow(() -> new NotExistException("Запрос не существует с этим id"));
        return ItemRequestMapper.mapToItemRequestDto(itemRequest);
    }

    private void isUserExists(Long userId) {
        log.info("Проверка существования пользователя с id {}", userId);
        boolean userExists = userRepository.existsById(userId);
        if (!userExists) {
            throw new NotExistException("Не существует пользователь с id %d", userId);
        }
    }
}
