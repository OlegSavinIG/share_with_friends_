package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserStorage userStorage;

    public boolean deleteUserById(Long userId) {
           return userStorage.deleteById(userId);
    }

    public List<User> getAllUsers() {
       return userStorage.getAllUsers();
    }

    public User getUserById(Long userId) {
        return userStorage.getById(userId).orElseThrow(() -> new RuntimeException());
    }

    public User updateUser(User user) {
        return userStorage.updateUser(user).orElseThrow(() -> new RuntimeException());
    }

    public User addUser(User user) {
        return userStorage.addUser(user);
    }

    public boolean isUserExist(long userId) {
        return getUserById(userId) != null;
    }
}
