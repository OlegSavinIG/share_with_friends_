package ru.practicum.shareit.user;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.*;

@Repository
public class InMemoryUserStorageImpl implements UserStorage {
    private final Map<Long, User> users = new HashMap<>();
    private long generatedId = 1;

    @Override
    public Optional<User> getById(Long userId) {
        return Optional.ofNullable(users.get(userId));
    }


    @Override
    public boolean deleteById(Long userId) {
        return users.remove(userId) != null;
    }

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User updateUser(UserDto userDto, long id) {
        User existUser = users.get(id);
        if (userDto.getName() != null) {
            existUser.setName(userDto.getName());
        }
        if (userDto.getEmail() != null) {
            existUser.setEmail(userDto.getEmail());
        }
        users.put(existUser.getId(), existUser);
        return existUser;
    }


    @Override
    public User addUser(User user) {
        user.setId(generatedId++);
        users.put(user.getId(), user);
        return user;
    }
}
