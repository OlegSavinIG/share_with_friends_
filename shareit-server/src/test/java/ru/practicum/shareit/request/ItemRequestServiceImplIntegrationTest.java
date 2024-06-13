package ru.practicum.shareit.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Transactional
@ExtendWith(SpringExtension.class)
public class ItemRequestServiceImplIntegrationTest {

    @Autowired
    private ItemRequestServiceImpl itemRequestService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRequestRepository itemRequestRepository;

    private User user1;
    private User user2;

    @BeforeEach
    public void setup() {
        user1 = User.builder()
                .name("Test User 1")
                .email("testuser1@example.com")
                .build();
        userRepository.save(user1);

        user2 = User.builder()
                .name("Test User 2")
                .email("testuser2@example.com")
                .build();
        userRepository.save(user2);

        ItemRequest itemRequest1 = ItemRequest.builder()
                .description("Test Item Request 1")
                .user(user1)
                .created(LocalDateTime.now())
                .build();

        ItemRequest itemRequest2 = ItemRequest.builder()
                .description("Test Item Request 2")
                .user(user2)
                .created(LocalDateTime.now())
                .build();

        itemRequestRepository.save(itemRequest1);
        itemRequestRepository.save(itemRequest2);
    }

    @Test
    public void testFindAllItemRequests() {
        List<ItemRequestDto> result = itemRequestService.findAllItemRequests(user1.getId(), 0, 10);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test Item Request 2", result.get(0).getDescription());
    }

    @Test
    public void testFindItemRequestsByUser() {
        List<ItemRequestDto> result = itemRequestService.findItemRequestsByUser(user1.getId());

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test Item Request 1", result.get(0).getDescription());
    }
}
