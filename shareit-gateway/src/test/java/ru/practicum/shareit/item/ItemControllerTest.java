package ru.practicum.shareit.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.practicum.shareit.exception.DataNotFoundException;
import ru.practicum.shareit.item.dto.CommentRequestDto;
import ru.practicum.shareit.item.dto.ItemRequestDto;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.*;

class ItemControllerTest {

    @Mock
    private ItemClient itemClient;

    @InjectMocks
    private ItemController itemController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateItem() {
        ItemRequestDto itemDto = new ItemRequestDto();
        Long userId = 1L;

        when(itemClient.createItem(any(ItemRequestDto.class), anyLong())).thenReturn(new ResponseEntity<>(HttpStatus.CREATED));

        ResponseEntity<Object> response = itemController.create(itemDto, userId);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        verify(itemClient, times(1)).createItem(itemDto, userId);
    }

    @Test
    void testCreateItemWithoutUserId() {
        ItemRequestDto itemDto = new ItemRequestDto();

        DataNotFoundException exception = assertThrows(DataNotFoundException.class, () -> {
            itemController.create(itemDto, null);
        });

        assertEquals("Не передан id пользователя", exception.getMessage());
    }

    @Test
    void testGetById() {
        Long itemId = 1L;
        Long userId = 1L;

        when(itemClient.getItem(anyLong(), anyLong())).thenReturn(new ResponseEntity<>(HttpStatus.OK));

        ResponseEntity<Object> response = itemController.getById(itemId, userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(itemClient, times(1)).getItem(itemId, userId);
    }

    @Test
    void testUpdateItem() {
        ItemRequestDto itemDto = new ItemRequestDto();
        Long itemId = 1L;
        Long userId = 1L;

        when(itemClient.updateItem(any(ItemRequestDto.class), anyLong(), anyLong())).thenReturn(new ResponseEntity<>(HttpStatus.OK));

        ResponseEntity<Object> response = itemController.update(itemDto, itemId, userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(itemClient, times(1)).updateItem(itemDto, itemId, userId);
    }

    @Test
    void testUpdateItemWithoutUserId() {
        ItemRequestDto itemDto = new ItemRequestDto();
        Long itemId = 1L;

        DataNotFoundException exception = assertThrows(DataNotFoundException.class, () -> {
            itemController.update(itemDto, itemId, null);
        });

        assertEquals("Не передан id пользователя", exception.getMessage());
    }

    @Test
    void testDeleteItem() {
        Long itemId = 1L;

        when(itemClient.deleteById(anyLong())).thenReturn(new ResponseEntity<>(HttpStatus.NO_CONTENT));

        ResponseEntity<Object> response = itemController.delete(itemId);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(itemClient, times(1)).deleteById(itemId);
    }

    @Test
    void testGetAllItems() {
        Long userId = 1L;
        int from = 0;
        int size = 10;

        when(itemClient.getAllItems(anyLong(), anyInt(), anyInt())).thenReturn(new ResponseEntity<>(HttpStatus.OK));

        ResponseEntity<Object> response = itemController.getAll(userId, from, size);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(itemClient, times(1)).getAllItems(userId, from, size);
    }

    @Test
    void testSearchByNameOrDescription() {
        Long userId = 1L;
        String text = "item";
        int from = 0;
        int size = 10;

        when(itemClient.searchByNameOrDescription(anyString(), anyLong(), anyInt(), anyInt())).thenReturn(new ResponseEntity<>(HttpStatus.OK));

        ResponseEntity<Object> response = itemController.searchByNameOrDescription(userId, text, from, size);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(itemClient, times(1)).searchByNameOrDescription(text, userId, from, size);
    }

    @Test
    void testSearchByNameOrDescriptionWithBlankText() {
        Long userId = 1L;
        String text = "";
        int from = 0;
        int size = 10;

        ResponseEntity<Object> response = itemController.searchByNameOrDescription(userId, text, from, size);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody() instanceof Collections);
        verify(itemClient, never()).searchByNameOrDescription(anyString(), anyLong(), anyInt(), anyInt());
    }

    @Test
    void testCreateComment() {
        Long itemId = 1L;
        Long userId = 1L;
        CommentRequestDto commentDto = new CommentRequestDto();

        when(itemClient.createComment(anyLong(), anyLong(), any(CommentRequestDto.class))).thenReturn(new ResponseEntity<>(HttpStatus.CREATED));

        ResponseEntity<Object> response = itemController.createComment(itemId, userId, commentDto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        verify(itemClient, times(1)).createComment(itemId, userId, commentDto);
    }
}
