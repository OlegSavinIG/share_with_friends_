package ru.practicum.shareit.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class ItemRepositoryTest {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private UserRepository userRepository;

    private User user1;
    private Item item1;
    private Item item2;

    @BeforeEach
    void setUp() {
        user1 = new User();
        user1.setName("User1");
        user1.setEmail("user1@example.com");
        user1 = userRepository.save(user1);

        item1 = new Item();
        item1.setName("Item1");
        item1.setDescription("Description of Item1");
        item1.setAvailable(true);
        item1.setUser(user1);
        item1 = itemRepository.save(item1);

        item2 = new Item();
        item2.setName("Item2");
        item2.setDescription("Description of Item2");
        item2.setAvailable(true);
        item2.setUser(user1);
        item2 = itemRepository.save(item2);
    }

    @Test
    void testFindByNameOrDescriptionContainingIgnoreCaseAndAvailable() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Item> items = itemRepository.findByNameOrDescriptionContainingIgnoreCaseAndAvailable("Item", pageable);

        assertThat(items).hasSize(2);
        assertThat(items.getContent()).extracting("name").containsExactlyInAnyOrder("Item1", "Item2");
    }

    @Test
    void testFindAvailableById() {
        Boolean available = itemRepository.findAvailableById(item1.getId());

        assertThat(available).isTrue();
    }

    @Test
    void testFindAllByUserIdOrderByBookingStartDesc() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Item> items = itemRepository.findAllByUserIdOrderByBookingStartDesc(user1.getId(), pageable);

        assertThat(items).hasSize(2);
        assertThat(items.getContent()).extracting("name").containsExactlyInAnyOrder("Item1", "Item2");
    }
}
