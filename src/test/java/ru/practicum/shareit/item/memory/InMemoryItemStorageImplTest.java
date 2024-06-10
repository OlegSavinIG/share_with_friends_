package ru.practicum.shareit.item.memory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class InMemoryItemStorageImplTest {

    private InMemoryItemStorageImpl itemStorage;
    private Item item;
    private ItemDto itemDto;

    @BeforeEach
    public void setup() {
        itemStorage = new InMemoryItemStorageImpl();

        item = Item.builder()
                .name("Test Item")
                .description("Test Description")
                .available(true)
                .build();

        itemDto = ItemDto.builder()
                .id(1L)
                .name("Updated Item")
                .description("Updated Description")
                .available(false)
                .build();
    }

    @Test
    public void testAddItem() {
        Item result = itemStorage.addItem(item);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Test Item", result.getName());
        assertEquals("Test Description", result.getDescription());
        assertTrue(result.getAvailable());
    }

    @Test
    public void testGetItemById() {
        itemStorage.addItem(item);
        Optional<Item> result = itemStorage.getItemById(1L);

        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
        assertEquals("Test Item", result.get().getName());
    }

    @Test
    public void testUpdateItem() {
        itemStorage.addItem(item);
        itemDto.setId(1L);

        Item updatedItem = itemStorage.updateItem(itemDto);

        assertNotNull(updatedItem);
        assertEquals(1L, updatedItem.getId());
        assertEquals("Updated Item", updatedItem.getName());
        assertEquals("Updated Description", updatedItem.getDescription());
        assertFalse(updatedItem.getAvailable());
    }

    @Test
    public void testDeleteItemById() {
        itemStorage.addItem(item);

        itemStorage.deleteItemById(1L);
        Optional<Item> result = itemStorage.getItemById(1L);

        assertFalse(result.isPresent());
    }

    @Test
    public void testGetAllItems() {
        itemStorage.addItem(item);
        List<Item> items = itemStorage.getAllItems(List.of(1L));

        assertEquals(1, items.size());
        assertEquals(1L, items.get(0).getId());
    }

    @Test
    public void testSearchByNameOrDescription() {
        itemStorage.addItem(item);
        List<Item> items = itemStorage.searchByNameOrDescription("Test", 1L);

        assertEquals(1, items.size());
        assertEquals(1L, items.get(0).getId());
    }

    @Test
    public void testSearchByNameOrDescriptionNoMatch() {
        itemStorage.addItem(item);
        List<Item> items = itemStorage.searchByNameOrDescription("Nonexistent", 1L);

        assertTrue(items.isEmpty());
    }
}
