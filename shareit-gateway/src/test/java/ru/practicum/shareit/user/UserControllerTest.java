package ru.practicum.shareit.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class UserControllerTest {

    @Mock
    private UserClient userClient;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void create_Success() {
        UserRequestDto userRequestDto = new UserRequestDto("John Doe", "john.doe@example.com");
        ResponseEntity<Object> expectedResponse = new ResponseEntity<>(HttpStatus.OK);

        when(userClient.createUser(any(UserRequestDto.class))).thenReturn(expectedResponse);

        ResponseEntity<Object> response = userController.create(userRequestDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(userClient, times(1)).createUser(userRequestDto);
    }


    @Test
    void getById_Success() {
        Long id = 1L;
        ResponseEntity<Object> expectedResponse = new ResponseEntity<>(HttpStatus.OK);

        when(userClient.getUserById(anyLong())).thenReturn(expectedResponse);

        ResponseEntity<Object> response = userController.getById(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(userClient, times(1)).getUserById(id);
    }

    @Test
    void update_Success() {
        Long id = 1L;
        UserRequestDto userRequestDto = new UserRequestDto("Jane Doe", "jane.doe@example.com");
        ResponseEntity<Object> expectedResponse = new ResponseEntity<>(HttpStatus.OK);

        when(userClient.updateUser(any(UserRequestDto.class), anyLong())).thenReturn(expectedResponse);

        ResponseEntity<Object> response = userController.update(userRequestDto, id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(userClient, times(1)).updateUser(userRequestDto, id);
    }


    @Test
    void delete_Success() {
        Long id = 1L;
        ResponseEntity<Object> expectedResponse = new ResponseEntity<>(HttpStatus.OK);

        when(userClient.deleteById(anyLong())).thenReturn(expectedResponse);

        ResponseEntity<Object> response = userController.delete(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(userClient, times(1)).deleteById(id);
    }

    @Test
    void getAll_Success() {
        ResponseEntity<Object> expectedResponse = new ResponseEntity<>(HttpStatus.OK);

        when(userClient.getAllUsers()).thenReturn(expectedResponse);

        ResponseEntity<Object> response = userController.getAll();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(userClient, times(1)).getAllUsers();
    }
}
