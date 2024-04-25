package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.base.BaseController;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController extends BaseController<UserDto, Long> {
    private final UserService userService;

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
    protected boolean deleteEntity(Long id) {
       return userService.deleteUserById(id);
    }

    @Override
    protected List<UserDto> getAllEntities(Long userId) {
        return userService.getAllUsers();
    }
//    @PostMapping
//    public ResponseEntity<User> addUser(@RequestBody User user) {
//        User createdUser = userService.addUser(user);
//        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
//    }
//
//    @PatchMapping
//    public ResponseEntity<User> updateUser(@RequestBody User user) {
//        User updatedUser = userService.updateUser(user);
//        return updatedUser != null ? ResponseEntity.ok(updatedUser) : ResponseEntity.notFound().build();
//    }
//
//    @GetMapping("/{userId}")
//    public ResponseEntity<User> getUserById(@PathVariable Long userId) {
//        if (userId == null) {
//            return ResponseEntity.badRequest().build(); // Обработка случая, когда ID не предоставлен
//        }
//        User user = userService.getUserById(userId);
//        return user != null ? ResponseEntity.ok(user) : ResponseEntity.notFound().build();
//    }
//
//    @GetMapping
//    public ResponseEntity<List<User>> getAllUsers() {
//        List<User> users = userService.getAllUsers();
//        return ResponseEntity.ok(users);
//    }
//
//    @DeleteMapping("/{userId}")
//    public ResponseEntity<Void> deleteUserById(@PathVariable Long userId) {
//        if (userId == null) {
//            return ResponseEntity.badRequest().build();
//        }
//        boolean deleted = userService.deleteUserById(userId);
//        return deleted ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
//    }

}
