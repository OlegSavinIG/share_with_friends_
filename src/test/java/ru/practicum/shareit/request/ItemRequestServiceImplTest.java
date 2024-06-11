package ru.practicum.shareit.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.exception.NotExistException;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestMapper;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ItemRequestServiceImplTest {

    @InjectMocks
    private ItemRequestServiceImpl itemRequestService;

    @Mock
    private ItemRequestRepository itemRequestRepository;

    @Mock
    private UserRepository userRepository;

    private User user;
    private ItemRequest itemRequest;
    private ItemRequestDto itemRequestDto;

    @BeforeEach
    public void setup() {
        user = User.builder()
                .id(1L)
                .name("Test User")
                .email("test@example.com")
                .build();

        itemRequest = ItemRequest.builder()
                .id(1L)
                .description("Need a drill")
                .created(LocalDateTime.now())
                .user(user)
                .build();

        itemRequestDto = ItemRequestMapper.mapToItemRequestDto(itemRequest);
    }

    @Test
    public void testCreateItemRequestSuccess() {
        when(userRepository.existsById(user.getId())).thenReturn(true);
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(itemRequestRepository.save(any(ItemRequest.class))).thenReturn(itemRequest);

        ItemRequestDto result = itemRequestService.createItemRequest(itemRequestDto, user.getId());

        assertNotNull(result);
        assertEquals(itemRequestDto.getDescription(), result.getDescription());
        verify(itemRequestRepository, times(1)).save(any(ItemRequest.class));
    }

    @Test
    public void testCreateItemRequestUserNotFound() {
        when(userRepository.existsById(user.getId())).thenReturn(false);

        NotExistException exception = assertThrows(NotExistException.class, () -> {
            itemRequestService.createItemRequest(itemRequestDto, user.getId());
        });

        assertEquals("Не существует пользователь с id 1", exception.getMessage());
    }

    @Test
    public void testFindItemRequestsByUser() {
        when(userRepository.existsById(user.getId())).thenReturn(true);
        when(itemRequestRepository.findByUserId(user.getId())).thenReturn(Collections.singletonList(itemRequest));

        List<ItemRequestDto> result = itemRequestService.findItemRequestsByUser(user.getId());

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(itemRequest.getDescription(), result.get(0).getDescription());
    }

    @Test
    public void testFindItemRequestsByUserUserNotFound() {
        when(userRepository.existsById(user.getId())).thenReturn(false);

        NotExistException exception = assertThrows(NotExistException.class, () -> {
            itemRequestService.findItemRequestsByUser(user.getId());
        });

        assertEquals("Не существует пользователь с id 1", exception.getMessage());
    }

    @Test
    public void testFindAllItemRequests() {
        PageRequest pageable = PageRequest.of(0, 10);
        Page<ItemRequest> itemRequestPage = new PageImpl<>(Collections.singletonList(itemRequest));
        when(userRepository.existsById(user.getId())).thenReturn(true);
        when(itemRequestRepository.findAllExcludingUserId(eq(user.getId()), any(PageRequest.class)))
                .thenReturn(itemRequestPage);

        List<ItemRequestDto> result = itemRequestService.findAllItemRequests(user.getId(), 0, 10);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(itemRequest.getDescription(), result.get(0).getDescription());
    }

    @Test
    public void testFindAllItemRequestsUserNotFound() {
        when(userRepository.existsById(user.getId())).thenReturn(false);

        NotExistException exception = assertThrows(NotExistException.class, () -> {
            itemRequestService.findAllItemRequests(user.getId(), 0, 10);
        });

        assertEquals("Не существует пользователь с id 1", exception.getMessage());
    }

    @Test
    public void testFindById() {
        when(userRepository.existsById(user.getId())).thenReturn(true);
        when(itemRequestRepository.findById(itemRequest.getId())).thenReturn(Optional.of(itemRequest));

        ItemRequestDto result = itemRequestService.findById(itemRequest.getId(), user.getId());

        assertNotNull(result);
        assertEquals(itemRequest.getDescription(), result.getDescription());
    }

    @Test
    public void testFindByIdNotFound() {
        when(userRepository.existsById(user.getId())).thenReturn(true);
        when(itemRequestRepository.findById(itemRequest.getId())).thenReturn(Optional.empty());

        NotExistException exception = assertThrows(NotExistException.class, () -> {
            itemRequestService.findById(itemRequest.getId(), user.getId());
        });

        assertEquals("Запрос не существует с этим id", exception.getMessage());
    }
}
