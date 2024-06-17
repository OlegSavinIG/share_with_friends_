package ru.practicum.shareit.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class ItemRequestControllerTest {

    @Mock
    private ItemRequestClient itemRequestClient;

    @InjectMocks
    private ItemRequestController itemRequestController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createRequest_Success() {
        Long userId = 1L;
        ItemRequestDto itemRequestDto = new ItemRequestDto(1l,"Item request description");
        ResponseEntity<Object> expectedResponse = new ResponseEntity<>(HttpStatus.OK);

        when(itemRequestClient.createItemRequest(any(ItemRequestDto.class), anyLong())).thenReturn(expectedResponse);

        ResponseEntity<Object> response = itemRequestController.createRequest(itemRequestDto, userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(itemRequestClient, times(1)).createItemRequest(itemRequestDto, userId);
    }


    @Test
    void findItemRequestsByUser_Success() {
        Long userId = 1L;
        ResponseEntity<Object> expectedResponse = new ResponseEntity<>(HttpStatus.OK);

        when(itemRequestClient.findItemRequestsByUser(anyLong())).thenReturn(expectedResponse);

        ResponseEntity<Object> response = itemRequestController.findItemRequestsByUser(userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(itemRequestClient, times(1)).findItemRequestsByUser(userId);
    }

    @Test
    void findAllItemRequests_Success() {
        Long userId = 1L;
        int from = 0;
        int size = 10;
        ResponseEntity<Object> expectedResponse = new ResponseEntity<>(HttpStatus.OK);

        when(itemRequestClient.findAllItemRequests(anyLong(), anyInt(), anyInt())).thenReturn(expectedResponse);

        ResponseEntity<Object> response = itemRequestController.findAllItemRequests(userId, from, size);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(itemRequestClient, times(1)).findAllItemRequests(userId, from, size);
    }

    @Test
    void findById_Success() {
        Long requestId = 1L;
        Long userId = 1L;
        ResponseEntity<Object> expectedResponse = new ResponseEntity<>(HttpStatus.OK);

        when(itemRequestClient.findById(anyLong(), anyLong())).thenReturn(expectedResponse);

        ResponseEntity<Object> response = itemRequestController.findById(requestId, userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(itemRequestClient, times(1)).findById(requestId, userId);
    }
}
