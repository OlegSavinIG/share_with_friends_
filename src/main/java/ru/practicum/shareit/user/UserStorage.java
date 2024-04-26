package ru.practicum.shareit.user;

import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Optional;

public interface UserStorage {
    Optional<User> getById(Long userId);

    boolean deleteById(Long userId);

    List<User> getAllUsers();

    User updateUser(User user, long id);

    User addUser(User user);
}
