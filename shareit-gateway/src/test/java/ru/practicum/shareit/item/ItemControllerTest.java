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
import ru.practicum.shareit.item.dto.ItemDto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class ItemControllerTest {

    @Mock
    private ItemClient itemClient;

    @InjectMocks
    private ItemController itemController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createSuccess() {
        Long userId = 1L;
        ItemDto itemDto = new ItemDto(1L, "Item Name", true, "Item Description", null);
        ResponseEntity<Object> expectedResponse = new ResponseEntity<>(HttpStatus.OK);

        when(itemClient.createItem(any(ItemDto.class), anyLong())).thenReturn(expectedResponse);

        ResponseEntity<Object> response = itemController.create(itemDto, userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(itemClient, times(1)).createItem(itemDto, userId);
    }

    @Test
    void createMissingUserId() {
        ItemDto itemDto = new ItemDto(1L, "Item Name", true, "Item Description", null);

        DataNotFoundException thrown = assertThrows(DataNotFoundException.class, () -> {
            itemController.create(itemDto, null);
        });

        assertEquals("Не передан id пользователя", thrown.getMessage());
    }


    @Test
    void getByIdSuccess() {
        Long id = 1L;
        Long userId = 1L;
        ResponseEntity<Object> expectedResponse = new ResponseEntity<>(HttpStatus.OK);

        when(itemClient.getItem(anyLong(), anyLong())).thenReturn(expectedResponse);

        ResponseEntity<Object> response = itemController.getById(id, userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(itemClient, times(1)).getItem(id, userId);
    }

    @Test
    void updateSuccess() {
        Long id = 1L;
        Long userId = 1L;
        ItemDto itemDto = new ItemDto(1L, "Updated Item", true, "Updated Description", null);
        ResponseEntity<Object> expectedResponse = new ResponseEntity<>(HttpStatus.OK);

        when(itemClient.updateItem(any(ItemDto.class), anyLong(), anyLong())).thenReturn(expectedResponse);

        ResponseEntity<Object> response = itemController.update(itemDto, id, userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(itemClient, times(1)).updateItem(itemDto, id, userId);
    }

    @Test
    void updateMissingUserId() {
        Long id = 1L;
        ItemDto itemDto = new ItemDto(1L, "Updated Item", true, "Updated Description", null);

        DataNotFoundException thrown = assertThrows(DataNotFoundException.class, () -> {
            itemController.update(itemDto, id, null);
        });

        assertEquals("Не передан id пользователя", thrown.getMessage());
    }

    @Test
    void deleteSuccess() {
        Long id = 1L;
        ResponseEntity<Object> expectedResponse = new ResponseEntity<>(HttpStatus.OK);

        when(itemClient.deleteById(anyLong())).thenReturn(expectedResponse);

        ResponseEntity<Object> response = itemController.delete(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(itemClient, times(1)).deleteById(id);
    }

    @Test
    void getAllSuccess() {
        Long userId = 1L;
        int from = 0;
        int size = 10;
        ResponseEntity<Object> expectedResponse = new ResponseEntity<>(HttpStatus.OK);

        when(itemClient.getAllItems(anyLong(), anyInt(), anyInt())).thenReturn(expectedResponse);

        ResponseEntity<Object> response = itemController.getAll(userId, from, size);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(itemClient, times(1)).getAllItems(userId, from, size);
    }

    @Test
    void searchByNameOrDescriptionSuccess() {
        Long userId = 1L;
        String text = "search text";
        int from = 0;
        int size = 10;
        ResponseEntity<Object> expectedResponse = new ResponseEntity<>(HttpStatus.OK);

        when(itemClient.searchByNameOrDescription(anyString(), anyLong(), anyInt(), anyInt())).thenReturn(expectedResponse);

        ResponseEntity<Object> response = itemController.searchByNameOrDescription(userId, text, from, size);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(itemClient, times(1)).searchByNameOrDescription(text, userId, from, size);
    }


    @Test
    void createCommentSuccess() {
        Long itemId = 1L;
        Long userId = 1L;
        CommentRequestDto commentDto = new CommentRequestDto(1l,"Nice item!");
        ResponseEntity<Object> expectedResponse = new ResponseEntity<>(HttpStatus.OK);

        when(itemClient.createComment(anyLong(), anyLong(), any(CommentRequestDto.class))).thenReturn(expectedResponse);

        ResponseEntity<Object> response = itemController.createComment(itemId, userId, commentDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(itemClient, times(1)).createComment(itemId, userId, commentDto);
    }

}
