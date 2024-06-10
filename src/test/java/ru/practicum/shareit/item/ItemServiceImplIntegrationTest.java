package ru.practicum.shareit.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import javax.transaction.Transactional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
public class ItemServiceImplIntegrationTest {

    @Autowired
    private ItemService itemService;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    public void setup() {
        userRepository.deleteAll();
        itemRepository.deleteAll();

        user = User.builder()
                .name("Test User")
                .email("testuser@example.com")
                .build();
        user = userRepository.save(user);

        Item item1 = Item.builder()
                .name("Item 1")
                .description("Description 1")
                .available(true)
                .user(user)
                .build();

        Item item2 = Item.builder()
                .name("Item 2")
                .description("Description 2")
                .available(true)
                .user(user)
                .build();

        itemRepository.save(item1);
        itemRepository.save(item2);
    }

    @Test
    public void testGetAllItemsByUserId() {
        List<ItemDto> items = itemService.getAllItemsByUserId(user.getId(), 0, 10);

        assertFalse(items.isEmpty());
        assertEquals(2, items.size());
        assertEquals("Item 1", items.get(0).getName());
        assertEquals("Item 2", items.get(1).getName());
    }

    @Test
    public void testSearchByNameOrDescription() {
        List<ItemDto> items = itemService.searchByNameOrDescription("Item", user.getId(), 0, 10);

        assertFalse(items.isEmpty());
        assertEquals(2, items.size());
        assertEquals("Item 1", items.get(0).getName());
        assertEquals("Item 2", items.get(1).getName());

        items = itemService.searchByNameOrDescription("Description 1", user.getId(), 0, 10);

        assertFalse(items.isEmpty());
        assertEquals(1, items.size());
        assertEquals("Item 1", items.get(0).getName());
    }
}
