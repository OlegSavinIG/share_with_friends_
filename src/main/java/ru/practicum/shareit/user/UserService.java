package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotExistException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserDtoMapper;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserStorage userStorage;
    private final List<String> allUserEmails = new ArrayList<>();

    public boolean deleteUserById(Long userId) {
        allUserEmails.remove(getUserById(userId).getEmail());
           return userStorage.deleteById(userId);
    }

    public List<UserDto> getAllUsers() {
        List<User> allUsers = userStorage.getAllUsers();
        return allUsers.stream().map(UserDtoMapper::userToUserDto).collect(Collectors.toList());
    }

    public UserDto getUserById(Long userId) {
        return UserDtoMapper.userToUserDto(userStorage.getById(userId)
                .orElseThrow(() -> new NotExistException("Несуществует пользователь с id %d", userId)));
    }

    public UserDto updateUser(UserDto userDto, long id) {
        if (allUserEmails.contains(userDto.getEmail())) {
            throw new ValidationException("Пользователь с таким email: %s уже существует", userDto.getEmail());
        }
        User user = UserDtoMapper.userDtoToUser(userDto);
        if (userDto.getEmail() != null) {
           allUserEmails.remove(getUserById(id).getEmail());
           allUserEmails.add(userDto.getEmail());
        }
        return UserDtoMapper.userToUserDto(userStorage.updateUser(user, id));
    }

    public UserDto addUser(UserDto userDto) {
        if (allUserEmails.isEmpty()) {
           allUserEmails.addAll(getAllUsers().stream()
                   .map(userDto1 -> userDto1.getEmail()).collect(Collectors.toList()));
        }
        if (allUserEmails.contains(userDto.getEmail())) {
            throw new ValidationException("Пользователь с таким email: %s уже существует", userDto.getEmail());
        }
        User user = UserDtoMapper.userDtoToUser(userDto);
        allUserEmails.add(userDto.getEmail());
        return UserDtoMapper.userToUserDto(userStorage.addUser(user));
    }

    public boolean isUserExist(long userId) {
        return getUserById(userId) != null;
    }
}
