package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.base.BaseController;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.memory.inMemoryUserService;

import java.util.List;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController extends BaseController<UserDto, Long> {
    private final inMemoryUserService userService;

    @Override
    protected UserDto createEntity(UserDto userDto, Long userId) {
        return userService.addUser(userDto);
    }

    @Override
    protected UserDto getEntityById(Long id) {
        return userService.getUserById(id);
    }

    @Override
    protected UserDto updateEntity(UserDto userDto, Long id, Long userId) {
        return userService.updateUser(userDto, id);
    }

    @Override
    protected void deleteEntity(Long id) {
         userService.deleteUserById(id);
    }

    @Override
    protected List<UserDto> getAllEntities(Long userId) {
        return userService.getAllUsers();
    }
}