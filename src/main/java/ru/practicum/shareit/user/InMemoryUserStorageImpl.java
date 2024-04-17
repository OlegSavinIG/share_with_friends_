package ru.practicum.shareit.user;

import ru.practicum.shareit.user.model.User;

import java.util.*;

public class InMemoryUserStorageImpl implements UserStorage {
    private Map<Long, User> users = new HashMap<>();
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
    public Optional<User> updateUser(User user) {
        User userOrDefault = users.getOrDefault(user.getId(), null);
        if (userOrDefault == null) {
            return Optional.empty();
        }
        return Optional.of(users.put(user.getId(), user));
    }

    @Override
    public User addUser(User user) {
        user.setId(generatedId++);
        return  users.put(user.getId(), user);
    }
}
