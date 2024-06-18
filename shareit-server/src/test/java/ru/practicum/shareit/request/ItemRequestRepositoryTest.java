package ru.practicum.shareit.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class ItemRequestRepositoryTest {

    @Autowired
    private ItemRequestRepository itemRequestRepository;

    @Autowired
    private UserRepository userRepository;

    private User user1;
    private User user2;
    private ItemRequest itemRequest1;
    private ItemRequest itemRequest2;

    @BeforeEach
    void setUp() {
        user1 = new User();
        user1.setName("User1");
        user1.setEmail("user1@example.com");
        user1 = userRepository.save(user1);

        user2 = new User();
        user2.setName("User2");
        user2.setEmail("user2@example.com");
        user2 = userRepository.save(user2);

        itemRequest1 = new ItemRequest();
        itemRequest1.setUser(user1);
        itemRequest1.setDescription("Request 1");
        itemRequest1 = itemRequestRepository.save(itemRequest1);

        itemRequest2 = new ItemRequest();
        itemRequest2.setUser(user2);
        itemRequest2.setDescription("Request 2");
        itemRequest2 = itemRequestRepository.save(itemRequest2);
    }

    @Test
    void testFindByUserId() {
        List<ItemRequest> requests = itemRequestRepository.findByUserId(user1.getId());
        assertThat(requests).hasSize(1);
        assertThat(requests.get(0).getDescription()).isEqualTo("Request 1");
    }

    @Test
    void testFindAllExcludingUserId() {
        Page<ItemRequest> requestsPage = itemRequestRepository.findAllExcludingUserId(user1.getId(), PageRequest.of(0, 10));
        assertThat(requestsPage.getTotalElements()).isEqualTo(1);
        assertThat(requestsPage.getContent().get(0).getDescription()).isEqualTo("Request 2");
    }
}
