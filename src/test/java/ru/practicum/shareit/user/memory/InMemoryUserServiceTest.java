package ru.practicum.shareit.user.memory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exception.NotExistException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class InMemoryUserServiceTest {

    @InjectMocks
    private InMemoryUserService userService;

    @Mock
    private UserStorage userStorage;

    private User user;
    private UserDto userDto;

    @BeforeEach
    public void setup() {
        user = User.builder()
                .id(1L)
                .name("Test User")
                .email("test@example.com")
                .build();

        userDto = UserDto.builder()
                .id(1L)
                .name("Test User")
                .email("test@example.com")
                .build();
    }

    @Test
    public void testAddUserSuccess() {
        when(userStorage.addUser(any(User.class))).thenReturn(user);

        UserDto result = userService.addUser(userDto);

        assertNotNull(result);
        assertEquals(userDto.getName(), result.getName());
        assertEquals(userDto.getEmail(), result.getEmail());
        verify(userStorage, times(1)).addUser(any(User.class));
    }

    @Test
    public void testAddUserWithExistingEmail() {
        when(userStorage.getAllUsers()).thenReturn(List.of(user));

        ValidationException exception = assertThrows(ValidationException.class, () -> {
            userService.addUser(userDto);
        });

        assertEquals("Пользователь с таким email: test@example.com уже существует", exception.getMessage());
    }

    @Test
    public void testUpdateUserSuccess() {
        User updatedUser = User.builder()
                .id(1L)
                .name("Updated User")
                .email("updated@example.com")
                .build();

        UserDto updatedUserDto = UserDto.builder()
                .id(1L)
                .name("Updated User")
                .email("updated@example.com")
                .build();

        when(userStorage.getById(anyLong())).thenReturn(Optional.of(user));
        when(userStorage.updateUser(any(UserDto.class), anyLong())).thenReturn(updatedUser);

        UserDto result = userService.updateUser(updatedUserDto, 1L);

        assertNotNull(result);
        assertEquals(updatedUserDto.getName(), result.getName());
        assertEquals(updatedUserDto.getEmail(), result.getEmail());
        verify(userStorage, times(1)).updateUser(any(UserDto.class), anyLong());
    }


    @Test
    public void testDeleteUserByIdNotExist() {
        when(userStorage.getById(anyLong())).thenReturn(Optional.empty());

        NotExistException exception = assertThrows(NotExistException.class, () -> {
            userService.deleteUserById(1L);
        });

        assertEquals("Не существует пользователь с id 1", exception.getMessage());
    }

    @Test
    public void testGetAllUsers() {
        when(userStorage.getAllUsers()).thenReturn(List.of(user));

        List<UserDto> result = userService.getAllUsers();

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(userDto.getName(), result.get(0).getName());
        verify(userStorage, times(1)).getAllUsers();
    }

    @Test
    public void testGetUserByIdSuccess() {
        when(userStorage.getById(anyLong())).thenReturn(Optional.of(user));

        UserDto result = userService.getUserById(1L);

        assertNotNull(result);
        assertEquals(userDto.getName(), result.getName());
        verify(userStorage, times(1)).getById(anyLong());
    }

    @Test
    public void testGetUserByIdNotExist() {
        when(userStorage.getById(anyLong())).thenReturn(Optional.empty());

        NotExistException exception = assertThrows(NotExistException.class, () -> {
            userService.getUserById(1L);
        });

        assertEquals("Не существует пользователь с id 1", exception.getMessage());
    }

    @Test
    public void testIsUserExistSuccess() {
        when(userStorage.getById(anyLong())).thenReturn(Optional.of(user));

        assertDoesNotThrow(() -> userService.isUserExist(1L));
    }

    @Test
    public void testIsUserExistNotExist() {
        when(userStorage.getById(anyLong())).thenReturn(Optional.empty());

        NotExistException exception = assertThrows(NotExistException.class, () -> {
            userService.isUserExist(1L);
        });

        assertEquals("Не существует пользователь с id 1", exception.getMessage());
    }
}
