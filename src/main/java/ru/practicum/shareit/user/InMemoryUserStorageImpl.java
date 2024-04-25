package ru.practicum.shareit.user;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.model.User;

import java.lang.reflect.Field;
import java.util.*;

@Repository
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
    public User updateUser(User user, long id) {
        try {
            User existUser = users.get(id);
            Field[] declaredFields = user.getClass().getDeclaredFields();
            for (Field declaredField : declaredFields) {
                declaredField.setAccessible(true);
                Object value = declaredField.get(user);
                if (value != null) {
                    Field existField = existUser.getClass().getDeclaredField(declaredField.getName());
                    existField.setAccessible(true);
                    existField.set(existUser, value);
                }
            }
            users.put(existUser.getId(), existUser);
            return existUser;
        } catch (IllegalAccessException | NoSuchFieldException | SecurityException e) {
            System.err.println("Ошибка при обновлении элемента: " + e.getMessage());
            return null;
        }
    }
//        User userOrDefault = users.getOrDefault(user.getId(), null);
//        if (userOrDefault == null) {
//            return Optional.empty();
//        }
//        return Optional.of(users.put(user.getId(), user));


    @Override
    public User addUser(User user) {
        user.setId(generatedId++);
        users.put(user.getId(), user);
        return user;
    }
}
