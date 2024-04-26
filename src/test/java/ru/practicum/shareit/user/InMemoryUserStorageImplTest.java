package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Map;
@RequiredArgsConstructor
class InMemoryUserStorageImplTest {
    private User user;
    private final InMemoryUserStorageImpl userStorage;

    @BeforeEach
    void setUp() {
      user = User.builder()
              .id(1l)
              .name("testName")
              .email("test@test.ru")
              .build();
    }

    @Test
    void getById() {
     List<User> allUsers = userStorage.getAllUsers();

    }

    @Test
    void deleteById() {

    }

    @Test
    void getAllUsers() {
    }

    @Test
    void updateUser() {
    }

    @Test
    void addUser() {
    }
}