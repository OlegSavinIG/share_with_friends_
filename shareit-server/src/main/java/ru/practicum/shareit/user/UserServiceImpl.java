package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotExistException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.model.UserDto;
import ru.practicum.shareit.user.model.UserDtoMapper;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    @Transactional
    public void deleteUserById(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotExistException("Не существует пользователь с этим id");
        }
        userRepository.deleteById(userId);
    }

    @Override
    public List<UserDto> getAllUsers() {
        List<User> allUsers = userRepository.findAll();
        return allUsers.stream()
                .map(UserDtoMapper::userToUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto getUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotExistException("Не существует пользователь с id %d", userId));
        return UserDtoMapper.userToUserDto(user);
    }

    @Override
    public UserDto updateUser(UserDto userDto, long userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new NotExistException("Не существует пользователь с id %d", userId);
        }
        User existUser = user.get();
        if (userDto.getName() != null) {
            existUser.setName(userDto.getName());
        }
        if (userDto.getEmail() != null) {
            existUser.setEmail(userDto.getEmail());
        }
        userRepository.save(existUser);
        return UserDtoMapper.userToUserDto(existUser);
    }

    @Override
    public UserDto addUser(UserDto userDto) {
        User saveUser = userRepository.save(UserDtoMapper.userDtoToUser(userDto));
        return UserDtoMapper.userToUserDto(saveUser);
    }
}
