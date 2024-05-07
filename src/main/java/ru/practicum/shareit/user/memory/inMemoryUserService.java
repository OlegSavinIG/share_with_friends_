package ru.practicum.shareit.user.memory;

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
public class inMemoryUserService {

    private final UserStorage userStorage;
    private final List<String> allUserEmails = new ArrayList<>();

    public void deleteUserById(Long userId) {
        allUserEmails.remove(getUserById(userId).getEmail());
        userStorage.deleteById(userId);
    }

    public List<UserDto> getAllUsers() {
        List<User> allUsers = userStorage.getAllUsers();
        return allUsers.stream().map(UserDtoMapper::userToUserDto).collect(Collectors.toList());
    }

    public UserDto getUserById(Long userId) {
        return UserDtoMapper.userToUserDto(userStorage.getById(userId)
                .orElseThrow(() -> new NotExistException("Не существует пользователь с id %d", userId)));
    }

    public UserDto updateUser(UserDto userDto, long id) {
        if (userDto.getEmail() != null && userDto.getEmail().equals(getUserById(id).getEmail())) {
            return UserDtoMapper.userToUserDto(userStorage.updateUser(userDto, id));
        }
        if (allUserEmails.contains(userDto.getEmail())) {
            throw new ValidationException("Пользователь с таким email: %s уже существует", userDto.getEmail());
        }
        if (userDto.getEmail() != null) {
            allUserEmails.remove(getUserById(id).getEmail());
            allUserEmails.add(userDto.getEmail());
        }
        return UserDtoMapper.userToUserDto(userStorage.updateUser(userDto, id));
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

    public void isUserExist(long userId) {
        if (getUserById(userId) == null) {
            throw new NotExistException("Пользователь с таким id %d не существует", userId);
        }
    }
}
