package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserService {
   void deleteUserById(Long userId);
    List<UserDto> getAllUsers();
    UserDto getUserById(Long userId);
    UserDto updateUser(UserDto userDto, long userId);
    UserDto addUser(UserDto userDto);
}
