package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.annotation.Update;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {
    private final UserClient userClient;

    @PostMapping
    public ResponseEntity<Object> create(@Valid @RequestBody UserRequestDto userRequestDto) {
        return userClient.createUser(userRequestDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getById(@PathVariable Long id) {
        return userClient.getUserById(id);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> update(@Validated(Update.class) @RequestBody UserRequestDto userRequestDto,
                                          @PathVariable Long id) {
        return userClient.updateUser(userRequestDto, id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        return userClient.deleteById(id);
    }

    @GetMapping
    public ResponseEntity<Object> getAll() {
        return userClient.getAllUsers();
    }
}
