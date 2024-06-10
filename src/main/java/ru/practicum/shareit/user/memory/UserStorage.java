package ru.practicum.shareit.user.memory;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Optional;

public interface UserStorage {
    Optional<User> getById(Long userId);

    boolean deleteById(Long userId);

    List<User> getAllUsers();

    User updateUser(UserDto userDto, long id);

    User addUser(User user);
}
