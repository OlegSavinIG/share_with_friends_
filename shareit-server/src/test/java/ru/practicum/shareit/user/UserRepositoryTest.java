package ru.practicum.shareit.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.practicum.shareit.user.model.User;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    public void setup() {
        userRepository.deleteAll();

        user = User.builder()
                .name("Test User")
                .email("testuser@example.com")
                .build();

        user = userRepository.save(user);
    }

    @Test
    public void testFindById() {
        Optional<User> foundUser = userRepository.findById(user.getId());
        assertTrue(foundUser.isPresent());
        assertEquals(user.getName(), foundUser.get().getName());
    }

    @Test
    public void testSaveUser() {
        User newUser = User.builder()
                .name("New User")
                .email("newuser@example.com")
                .build();

        User savedUser = userRepository.save(newUser);
        assertNotNull(savedUser);
        assertNotNull(savedUser.getId());
        assertEquals(newUser.getName(), savedUser.getName());
    }

    @Test
    public void testDeleteUser() {
        userRepository.deleteById(user.getId());
        Optional<User> deletedUser = userRepository.findById(user.getId());
        assertFalse(deletedUser.isPresent());
    }

    @Test
    public void testUpdateUser() {
        user.setName("Updated User");
        User updatedUser = userRepository.save(user);
        assertNotNull(updatedUser);
        assertEquals("Updated User", updatedUser.getName());
    }
}
