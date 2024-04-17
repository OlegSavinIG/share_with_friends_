package ru.practicum.shareit.user;

import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Optional;

public interface UserStorage {
    Optional<User> getById(Long userId);

    boolean deleteById(Long userId);

    List<User> getAllUsers();

    Optional<User> updateUser(User user);

    User addUser(User user);
}
